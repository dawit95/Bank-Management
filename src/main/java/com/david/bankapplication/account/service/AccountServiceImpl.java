package com.david.bankapplication.account.service;

import com.david.bankapplication.global.util.RestTemplateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileName : AccountServiceImpl
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 관리에 필요한 service Interface 구현체
 */

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final ObjectMapper objectMapper;

    private static SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String registerAccount(Long userId, String bankCode) {

//        RestTemplate restTemplate = new RestTemplate();

        //make HttpHeaders
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");


        //make Account
        String bankAccountNumber = accountGenerator();

        //make param
        Map<String, Object> map = new HashMap<>();
        map.put("bank_code", bankCode);
        map.put("bank_account_number", bankAccountNumber);

        //통신
        try {
            //make HttpEntity
            HttpEntity<?> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(map), httpHeaders);

            logger.debug("httpEntity 확인 header : {}", httpEntity.getHeaders());
            logger.debug("httpEntity 확인 body : {}", httpEntity.getBody());
            logger.debug("httpEntity 확인 : {}", httpEntity);

            ResponseEntity<String> responseEntity = RestTemplateUtil.getAccountResponseEntity(httpEntity);

            logger.debug("응답 코드 : {}", responseEntity.getStatusCode());
            logger.debug("응답 결과 출력 : {}", responseEntity.getBody());
        } catch (JsonProcessingException e) {
            logger.debug("HttpEntity 생성 예외 발생 : {}", e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            logger.debug("RestTemplate 예외 발생 : {}", e.toString());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * okhttp라이브러리 사용한 restTemplate 구현
     *  응답 Content-Type이 text/html; charset=utf-8로 설정 실패
     */
//    public String registerAccountVer1(Long userId, String bankCode) {
//
//        RestTemplate restTemplate = new RestTemplate();
//        OkHttpClient client = new OkHttpClient.Builder()
//                .readTimeout(5, TimeUnit.SECONDS)
//                .connectTimeout(7, TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(30, 10, TimeUnit.MINUTES)) //커넥션풀 적용
//                .build();
//        // @formatter:on
//        OkHttp3ClientHttpRequestFactory crf = new OkHttp3ClientHttpRequestFactory(client);
//        restTemplate.setRequestFactory(crf);
//
//        //make HttpHeaders
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//
//        //make Account
//        String bankAccountNumber = accountGenerator();
//
//        //make param
//        Map<String,Object> map = new HashMap<>();
//        map.put("bank_code", bankCode);
//        map.put("bank_account_number", bankAccountNumber);
//        String param = null;
//        try {
//            param = objectMapper.writeValueAsString(map);
//        } catch (JsonProcessingException e) {
//            logger.debug("Failed to create parameter!");
//            e.printStackTrace();
//        }
//
//        //make HttpEntity
//        HttpEntity httpEntity = new HttpEntity(param, httpHeaders);
//
//        logger.debug("httpEntity 확인 header : {}",httpEntity.getHeaders());
//        logger.debug("httpEntity 확인 body : {}",httpEntity.getBody());
//        logger.debug("httpEntity 확인 : {}",httpEntity);
//
//        //결과
//        try {
//            ResponseEntity<String> responseEntity = restTemplate.exchange(
//                    url+"/register",
//                    HttpMethod.POST,
//                    httpEntity,
//                    String.class);
//
//            logger.debug("응답 코드 : {}", responseEntity.getStatusCode());
//            logger.debug("응답 결과 출력 : {}", responseEntity.getBody());
//        }catch (Exception e){
//            logger.debug("예외 발생 : {}",e.toString());
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    /**
     * 기본 httpClient 사용한 restTemplate 구현
     * 응답 Content-Type이 text/html; charset=utf-8로 설정 실패
     */
//    public String registerAccountVer2(Long userId, String bankCode) {
//
//        //make HttpHeaders
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//
//
//        //make Account
//        String bankAccountNumber = accountGenerator();
//
//        //make param
//        Map<String,Object> map = new HashMap<>();
//        map.put("bank_code", bankCode);
//        map.put("bank_account_number", bankAccountNumber);
//        String param = null;
//        try {
//            param = objectMapper.writeValueAsString(map);
//        } catch (JsonProcessingException e) {
//            logger.debug("Failed to create parameter!");
//            e.printStackTrace();
//        }
//
//        //make HttpEntity
//        HttpEntity httpEntity = new HttpEntity(param, httpHeaders);
//
//        logger.debug("httpEntity 확인 header : {}",httpEntity.getHeaders());
//        logger.debug("httpEntity 확인 body : {}",httpEntity.getBody());
//        logger.debug("httpEntity 확인 : {}",httpEntity);
//
//        //RestTemplateUtil
//        ResponseEntity<String> responseEntity = RestTemplateUtil.getAccountResponseEntity(httpEntity);
//
//        logger.debug("code 확인 : {}",responseEntity.getStatusCode());
//        logger.debug("header 확인 : {}",responseEntity.getHeaders());
//        logger.debug("body 확인 : {}",responseEntity.getBody());
//
//        return null;
//    }
    @Override
    public String transferAccount(Long userId, String txId, Long fromAccountId, Long toAccountId, String comment, String amount) {
        return null;
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
        logger.debug("생성한 랜덤 계좌번호 : {}", randomStr);
        return randomStr.toString();
    }
}
