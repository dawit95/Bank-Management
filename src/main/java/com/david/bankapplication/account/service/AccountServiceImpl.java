package com.david.bankapplication.account.service;

import com.david.bankapplication.account.domain.Account;
import com.david.bankapplication.account.domain.AccountRepository;
import com.david.bankapplication.account.domain.TransactionLog;
import com.david.bankapplication.account.domain.TransactionLogRepository;
import com.david.bankapplication.account.dto.AccountDto;
import com.david.bankapplication.account.dto.RegisterResponseDto;
import com.david.bankapplication.account.dto.TransactionLogDto;
import com.david.bankapplication.account.dto.TransferResponseDto;
import com.david.bankapplication.global.dto.ErrorResponseDto;
import com.david.bankapplication.global.exception.AuthorizationException;
import com.david.bankapplication.global.exception.BankAPIException;
import com.david.bankapplication.global.exception.NoAccountException;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * FileName : AccountServiceImpl
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 관리에 필요한 service Interface 구현체
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionRepository;

    private final RestTemplate registerRestTemplate;
    private final ObjectMapper objectMapper;

    private static SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 계좌번호를 만든어 사용자 요청에 맞는 은행에 등록 (외부 API 사용)
     * 만들어진 계좌 DB에 저장
     *
     * @param userId   사용자 ID
     * @param bankCode 계좌생성을 원하는 bank_code
     * @return 생성한 계좌 AccountDto
     */
    @Transactional
    @Override
    public AccountDto registerAccount(Long userId, String bankCode) throws TemporarilyUnavailableException {
        Account targetAccount = Account.builder()
                .userId(userId)
                .bankCode(bankCode)
                .build();

        //make Account
        String bankAccountNumber = createAccountNumber(bankCode);

        //make param
        Map<String, Object> map = new HashMap<>();
        map.put("bank_code", bankCode);
        map.put("bank_account_number", bankAccountNumber);

        //통신
        try {
            //make HttpEntity
            HttpEntity<?> httpEntity = createHttpEntity(map);

            ResponseEntity<String> responseEntity = registerRestTemplate.exchange(
                    "/register",
                    HttpMethod.POST,
                    httpEntity,
                    String.class);

            if (responseEntity.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL) {
                RegisterResponseDto responseDto = objectMapper.readValue(responseEntity.getBody(), RegisterResponseDto.class);
                targetAccount.updateBankAccount(responseDto.getBank_account_id(), bankAccountNumber);
                accountRepository.save(targetAccount);
                return new AccountDto(targetAccount);
            }
        } catch (JsonProcessingException e) {
            log.debug("HttpEntity 생성 예외 발생 : {}", e.toString());
            e.printStackTrace();
        } catch (HttpStatusCodeException e) {
            ErrorResponseDto responseDto = HttpStatusCodeException(e);
            if (e.getStatusCode().series().equals(HttpStatus.Series.valueOf(400)) || e.getStatusCode().series().equals(HttpStatus.Series.valueOf(422))) {
                log.debug("HttpClientErrorException");
                throw new TemporarilyUnavailableException(responseDto.getMessage());
            } else if (e.getStatusCode().series().equals(HttpStatus.Series.valueOf(500))) {
                log.debug("HttpServerErrorException");
                throw new TemporarilyUnavailableException(responseDto.getMessage());
            }
        }
        throw new TemporarilyUnavailableException("서버의 예상하지 못한 에러발생! 잠시후 다시 시도해 주세요");
    }

    /**
     * 계좌 이체 (외부 API 사용)
     * 거래 기록 DB에 저장
     *
     * @param userId         사용자 ID
     * @Param from & to 계좌의 은행코드 & 번호
     * @param comment        거래 설명
     * @param transferAmount 거래 금액
     * @return 거래 기록 DTO
     */
    @Transactional
    @Override
    public TransactionLogDto transferAccount(
            Long userId,
            String fromAccountBankCode, String fromAccountBankNumber,
            String toAccountBankCode, String toAccountBankNumber,
            String comment, String transferAmount) throws NoAccountException, AuthorizationException, TemporarilyUnavailableException, BankAPIException {
        //유저가 가진 계좌 맞는지 확인
        Account fromAccount = accountRepository
                .findByBankCodeAndBankAccountNumber(fromAccountBankCode, fromAccountBankNumber)
                .orElseThrow(() -> new NoAccountException(fromAccountBankCode + "은행의 " + fromAccountBankNumber + " 계좌가 존재하지 않습니다."));
        if (Objects.equals(fromAccount.getUserId(), userId)) {
            throw new AuthorizationException("계좌의 정보를 다시 확인해주세요");
        }

        //상대 계좌가 존재하는지 확인
        Account toAccount = accountRepository
                .findByBankCodeAndBankAccountNumber(toAccountBankCode, toAccountBankNumber)
                .orElseThrow(() -> new NoAccountException(toAccountBankCode + "은행의 " + toAccountBankNumber + " 계좌가 존재하지 않습니다."));

        //make tx_id
        String txId = createTxId();

        //make param
        Map<String, Object> map = new HashMap<>();
        map.put("tx_id", txId);
        map.put("from_bank_account_id", fromAccount.getBankAccountId());
        map.put("to_bank_code", toAccountBankCode);
        map.put("to_bank_account_number", toAccountBankNumber);
        map.put("transfer_amount", transferAmount);

        //계좌 이체 요청
        try {
            //make HttpEntity
            HttpEntity<?> httpEntity = createHttpEntity(map);

            ResponseEntity<String> responseEntity = registerRestTemplate.exchange(
                    "/transfer",
                    HttpMethod.POST,
                    httpEntity,
                    String.class);

            if (responseEntity.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL) {
                TransferResponseDto responseDto = objectMapper.readValue(responseEntity.getBody(), TransferResponseDto.class);
                TransactionLog transactionLog = TransactionLog.builder()
                        .userId(userId)
                        .txId(txId)
                        .bankTxId(responseDto.getBank_tx_id())
                        .fromAccountId(fromAccount.getId())
                        .toAccountId(toAccount.getId())
                        .comment(comment)
                        .transferAmount(transferAmount)
                        .build();
                transactionRepository.save(transactionLog);
                TransactionLogDto transactionLogDto = new TransactionLogDto(transactionLog);
                transactionLogDto.setResult(responseDto.getResult());
                return transactionLogDto;
            }
        } catch (JsonProcessingException e) {
            log.debug("HttpEntity 생성 예외 발생 : {}", e.toString());
            e.printStackTrace();
        } catch (HttpStatusCodeException e) {
            ErrorResponseDto responseDto = HttpStatusCodeException(e);

            if (e.getStatusCode().series().equals(HttpStatus.Series.valueOf(400)) || e.getStatusCode().series().equals(HttpStatus.Series.valueOf(422))) {
                if (responseDto.getCode().equals("BANKING_ERROR_200")) {
                    throw new NoAccountException(fromAccountBankCode + "은행의 " + fromAccountBankNumber + " 계좌가 존재하지 않습니다.");
                } else if (responseDto.getCode().equals("BANKING_ERROR_201")) {
                    throw new NoAccountException(toAccountBankCode + "은행의 " + toAccountBankNumber + " 계좌가 존재하지 않습니다.");
                } else {
                    throw new BankAPIException(responseDto.getMessage());
                }

            } else if (e.getStatusCode().series().equals(HttpStatus.Series.valueOf(500))) {
                log.debug("HttpServerErrorException");
                throw new TemporarilyUnavailableException(responseDto.getMessage());
            }
        }
        throw new TemporarilyUnavailableException("서버의 예상하지 못한 에러발생! 잠시후 다시 시도해 주세요");
    }

    private ErrorResponseDto HttpStatusCodeException(HttpStatusCodeException e) {
        log.trace("HttpStatusCodeException");
        log.trace("exception body : {}", e.getResponseBodyAsString());
        ErrorResponseDto responseDto = new ErrorResponseDto();
        try {
            responseDto = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponseDto.class);
            log.debug("응답 확인 code : {}", responseDto.getCode());
            log.debug("응답 확인 message : {}", responseDto.getMessage());
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return responseDto;
    }

    /**
     * 외부 API 요청에 맞는 HttpEntity 생성
     *
     * @param map request body
     * @return HttpEntity
     */
    private HttpEntity<?> createHttpEntity(Map<String, Object> map) throws JsonProcessingException {
        //make HttpHeaders
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        return new HttpEntity<>(objectMapper.writeValueAsString(map), httpHeaders);
    }

    /**
     * 유효한 계좌번호 생성
     *
     * @param bankCode 은행코드 ["D001" || "D002" || "D003"]
     * @return 유효한 계좌번호 string
     */
    public String createAccountNumber(String bankCode) {
        String bankAccountNumber = accountGenerator(10);
        int cnt = 0;
        while (cnt < 10 && accountRepository.existsByBankCodeAndBankAccountNumber(bankCode, bankAccountNumber)) {
            bankAccountNumber = accountGenerator(10);
            //(timeout 같은 개념) 10번 시도후 불가능하면 Exception;
            cnt++;
            if (cnt == 10) {
                throw new RuntimeException("잠시후 다시 시도해 주세요!");
            }
        }
        return bankAccountNumber;
    }

    /**
     * 유일한 txId 생성
     *
     * @return 유일한 txId string
     */
    public String createTxId() {
        String txId = accountGenerator(8);
        int cnt = 0;
        while (cnt < 20 && transactionRepository.existsByTxId(txId)) {
            txId = accountGenerator(8);
            //(timeout 같은 개념) 20번 시도후 불가능하면 Exception;
            cnt++;
            if (cnt == 20) {
                throw new RuntimeException("잠시후 다시 시도해 주세요!");
            }
        }
        return txId;
    }

    /**
     * 랜덤한 번호를 만든다.
     *
     * @param numLength 번호 길이
     * @return 만들어진 번호 string
     */
    @Override
    public String accountGenerator(int numLength) {
        //랜던 문자 길이
        StringBuilder randomStr = new StringBuilder();

        for (int i = 0; i < numLength; i++) {
            //0 ~ 9 랜덤 숫자 생성
            randomStr.append(random.nextInt(10));
        }
        log.debug("생성한 랜덤 계좌번호 : {}", randomStr);
        return randomStr.toString();
    }
}
