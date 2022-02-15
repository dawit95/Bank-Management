package com.david.bankapplication.account.service;

import com.david.bankapplication.account.domain.Account;
import com.david.bankapplication.account.domain.AccountRepository;
import com.david.bankapplication.account.dto.RegisterResponseDto;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
     * 랜덤한 계좌번호를 만든다.
     *
     * @Param 사용자 ID
     * @Param 계좌생성을 원하는 bank_code
     * @return 만들어진 계좌번호 string
     */
    @Transactional
    @Override
    public String registerAccount(Long userId, String bankCode) throws TemporarilyUnavailableException {

        String account = "잠시후 다시 시도해 주세요!";

        //make HttpHeaders
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
//        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        //make Account
        String bankAccountNumber = checkingAccountValidity(bankCode);

        //make param
        Map<String, Object> map = new HashMap<>();
        map.put("bank_code", bankCode);
        map.put("bank_account_number", bankAccountNumber);

        //통신
        try {
            //make HttpEntity
            HttpEntity<?> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(map), httpHeaders);

            ResponseEntity<RegisterResponseDto> responseEntity = registerRestTemplate.exchange(
                    "/register",
                    HttpMethod.POST,
                    httpEntity,
                    RegisterResponseDto.class);

            if (responseEntity.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL) {
                Account targetAccount = Account.builder()
                        .userId(userId)
                        .bankCode(bankCode)
                        .bankAccountNumber(bankAccountNumber)
                        .bankAccountId(Objects.requireNonNull(responseEntity.getBody()).getBank_account_id())
                        .build();
                accountRepository.save(targetAccount);
                return bankAccountNumber;
            }
        } catch (JsonProcessingException e) {
            log.debug("HttpEntity 생성 예외 발생 : {}", e.toString());
            e.printStackTrace();
        } catch (HttpClientErrorException e){
            log.debug("log기록후 다시 도전");
        } catch (HttpServerErrorException e){
            throw new TemporarilyUnavailableException("외부 api 문제");
        }


            return bankAccountNumber;
    }

    @Override
    public String transferAccount(Long userId, String txId, Long fromAccountId, Long toAccountId, String comment, String amount) {
        return null;
    }

    /**
     * 계좌번호 유효성 확인
     *
     * @return 유효한 계좌번호 string
     */
    public String checkingAccountValidity(String bankCode) {
        String bankAccountNumber = accountGenerator();
        int cnt = 0;
        while (cnt < 10 && accountRepository.existsByBankCodeAndBankAccountNumber(bankCode, bankAccountNumber)) {
            bankAccountNumber = accountGenerator();
            //(timeout 같은 개념) 10번 시도후 불가능하면 Exception;
            cnt++;
            if (cnt == 10) {
                throw new RuntimeException("잠시후 다시 시도해 주세요!");
            }
        }
        return bankAccountNumber;
    }

    /**
     * 랜덤한 계좌번호를 만든다.
     *
     * @return 만들어진 계좌번호 string
     */
    @Override
    public String accountGenerator() {
        //랜던 문자 길이
        int numLength = 10;
        StringBuilder randomStr = new StringBuilder();

        for (int i = 0; i < numLength; i++) {
            //0 ~ 9 랜덤 숫자 생성
            randomStr.append(random.nextInt(10));
        }
        log.debug("생성한 랜덤 계좌번호 : {}", randomStr);
        return randomStr.toString();
    }
}
