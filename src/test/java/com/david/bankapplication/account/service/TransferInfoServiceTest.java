package com.david.bankapplication.account.service;

import com.david.bankapplication.account.dto.ResultDto;
import com.david.bankapplication.global.exception.BankAPIException;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * FileName : TransferInfoService
 * Author : David
 * Date : 2022-02-17
 * Description : 계좌이체 결과 확인 Account Service Test
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferInfoServiceTest {
    private RestTemplate restTemplate;
    private AccountServiceImpl accountService;
    private MockRestServiceServer mockServer;

//    private final String requestUri = "https://banking-api.sample.com/transfer";
//    private final String requestUri = "http://localhost:19999/banking-api/transfer";

    @Value("${my.rootUri}")
    private String requestUri;

    @Autowired
    public TransferInfoServiceTest(RestTemplate restTemplate, AccountServiceImpl accountService) {
        this.restTemplate = restTemplate;
        this.accountService = accountService;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("거래 내역 조회")
    void whenSuccess() throws TemporarilyUnavailableException, BankAPIException {
        //given
        mockServer
                .expect(requestTo(requestUri+"/transfer/12346578"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withStatus(HttpStatus.OK)
                        .body("{\"result\":\"SUCCESS\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        //when
        ResultDto resultDto = accountService.transferInfo("12346578");

        //then
        Assertions.assertThat(resultDto.getResult())
                .isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("거래 내역 조회 예외 상황")
    void whenFail() {
        //given
        mockServer
                .expect(requestTo(requestUri+"/transfer/12346578"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .body("{\"code\":\"BANKING_ERROR_200\", \"message\":\"잘못된 거래 ID\"}")
                        .contentType(MediaType.APPLICATION_JSON));

        try {
            //when
            ResultDto resultDto = accountService.transferInfo("12346578");
        } catch (TemporarilyUnavailableException e) {
            e.printStackTrace();
        } catch (BankAPIException e) {
            //then
            Assertions.assertThat(e.getMessage()).isEqualTo("잘못된 거래 ID");
        }

    }
}
