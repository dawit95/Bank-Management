package com.david.bankapplication.account.service;

import com.david.bankapplication.account.domain.Account;
import com.david.bankapplication.account.domain.AccountRepository;
import com.david.bankapplication.account.dto.AccountDto;
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
 * FileName : RegisterAccountServiceTest
 * Author : David
 * Date : 2022-02-15
 * Description : 계좌 등록 Service Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterAccountServiceTest {

    private RestTemplate restTemplate;
    private AccountServiceImpl accountService;
    private AccountRepository accountRepository;
    private MockRestServiceServer mockServer;


//    private final String requestUri = "https://banking-api.sample.com/register";
//    private final String requestUri = "http://localhost:19999/banking-api/register";
    @Value("${my.rootUri}")
    private String requestUri;

    @Autowired
    public RegisterAccountServiceTest(RestTemplate restTemplate, AccountServiceImpl accountService, AccountRepository accountRepository) {
        this.restTemplate = restTemplate;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("계좌 등록 성공")
    void whenSuccess() throws TemporarilyUnavailableException, BankAPIException {
        //given
        mockServer
                .expect(requestTo(requestUri+"/register"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withStatus(HttpStatus.OK)
                        .body("{\"bank_account_id\":\"12345678\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        //when
        AccountDto accountDto = accountService.registerAccount(1L, "D001");

        //then
        Account account = accountRepository.findByBankAccountId(accountDto.getBankAccountId()).orElse(Account.builder().build());

        Assertions.assertThat(accountDto.getUserId()).isEqualTo(account.getUserId());
        Assertions.assertThat(accountDto.getBankCode()).isEqualTo(account.getBankCode());
        Assertions.assertThat(accountDto.getBankAccountId()).isEqualTo(account.getBankAccountId());
        Assertions.assertThat(accountDto.getBankAccountNumber()).isEqualTo(account.getBankAccountNumber());
    }

    @Test
    @DisplayName("계좌 등록 실패")
    void whenFail() {
        //given
        mockServer
                .expect(requestTo(requestUri+"/register"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .body("{\"code\":\"BANKING_ERROR_100\", \"message\":\"잘못된 계좌 정보\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        //when
        try {
            AccountDto accountDto = accountService.registerAccount(1L, "D001");
        } catch (BankAPIException e) {
            //then
            Assertions.assertThat(e.getMessage()).isEqualTo("잘못된 계좌 정보");
        } catch (TemporarilyUnavailableException e) {
            e.printStackTrace();
        }

    }

}
