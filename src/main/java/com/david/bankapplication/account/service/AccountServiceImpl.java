package com.david.bankapplication.account.service;

import com.david.bankapplication.account.domain.Account;
import com.david.bankapplication.account.domain.AccountRepository;
import com.david.bankapplication.account.domain.TransactionLogRepository;
import com.david.bankapplication.account.dto.AccountDto;
import com.david.bankapplication.account.dto.RegisterResponseDto;
import com.david.bankapplication.global.dto.ErrorResponseDto;
import com.david.bankapplication.global.exception.AuthorizationException;
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
     * @Param 사용자 ID
     * @Param 계좌생성을 원하는 bank_code
     * @return 만들어진 계좌번호 string
     */
    @Transactional
    @Override
    public AccountDto registerAccount(Long userId, String bankCode) throws TemporarilyUnavailableException {
        Account targetAccount = Account.builder()
                .userId(userId)
                .bankCode(bankCode)
                .build();

        //make HttpHeaders
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        //make Account
        String bankAccountNumber = createAccountNumber(bankCode);

        //make param
        Map<String, Object> map = new HashMap<>();
        map.put("bank_code", bankCode);
        map.put("bank_account_number", bankAccountNumber);

        //통신
        try {
            //make HttpEntity
            HttpEntity<?> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(map), httpHeaders);

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
            log.trace("HttpStatusCodeException");
            log.trace("exception body : {}", e.getResponseBodyAsString());
            ErrorResponseDto responseDto = null;
            try {
                responseDto = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponseDto.class);
                log.debug("응답 확인 code : {}", responseDto.getCode());
                log.debug("응답 확인 message : {}", responseDto.getMessage());
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            if (e.getStatusCode().series().equals(HttpStatus.Series.valueOf(400)) || e.getStatusCode().series().equals(HttpStatus.Series.valueOf(422))) {
                log.debug("HttpClientErrorException");
                assert responseDto != null;
                throw new TemporarilyUnavailableException(responseDto.getMessage());
            } else if (e.getStatusCode().series().equals(HttpStatus.Series.valueOf(500))) {
                log.debug("HttpServerErrorException");
                assert responseDto != null;
                throw new TemporarilyUnavailableException(responseDto.getMessage());
            }
        }
        throw new TemporarilyUnavailableException("서버의 예상하지 못한 에러발생! 잠시후 다시 시도해 주세요");
    }

    /**
     * 계좌 이체 (외부 API 사용)
     * 거래 기록 DB에 저장
     *
     * @Param 사용자 ID
     * @Param 계좌 이체 from to 계좌 ID
     * @Param 거래 설명
     * @Param 거래 금액
     * @return 만들어진 계좌번호 string
     */
    @Transactional
    @Override
    public String transferAccount(
            Long userId,
            String fromAccountBankCode, String fromAccountBankNumber,
            String toAccountBankCode, String toAccountBankNumber,
            String comment, String transferAmount) throws NoAccountException, AuthorizationException {
        //유저가 가진 계좌 맞는지 알맞은 금액을 가졌는지 확인
        Account fromAccount = accountRepository
                .findByBankCodeAndBankAccountNumber(fromAccountBankCode,fromAccountBankNumber)
                .orElseThrow(()->new NoAccountException(fromAccountBankCode+"은행의 사용자 계좌가 존재하지 않습니다."));
        if(Objects.equals(fromAccount.getUserId(), userId)){
            throw new AuthorizationException("계좌의 정보를 다시 확인해주세요");
        }

        //상대 계좌가 존재하는지 확인

        //계좌 이체 요청

        //거래 내역 저장 및 결과 전달

        return null;
    }

    public String transferAccount(Long userId, Long fromAccountId, Long toAccountId, String comment, String amount) {


        return null;
    }

    /**
     * 유효한 계좌번호 생성
     * @Param 은행코드 ["D001" || "D002" || "D003"]
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
     * @Param 번호 길이
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
