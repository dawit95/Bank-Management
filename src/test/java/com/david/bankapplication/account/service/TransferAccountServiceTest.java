package com.david.bankapplication.account.service;

import com.david.bankapplication.account.domain.Account;
import com.david.bankapplication.account.domain.AccountRepository;
import com.david.bankapplication.account.domain.TransactionLog;
import com.david.bankapplication.account.domain.TransactionLogRepository;
import com.david.bankapplication.account.dto.TransactionLogDto;
import com.david.bankapplication.global.exception.AuthorizationException;
import com.david.bankapplication.global.exception.BankAPIException;
import com.david.bankapplication.global.exception.NoAccountException;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
 * FileName : TransferAccountServiceTest
 * Author : David
 * Date : 2022-02-17
 * Description : 계좌 이체 Account Service Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferAccountServiceTest {

    private RestTemplate restTemplate;
    private AccountServiceImpl accountService;

    private AccountRepository accountRepository;
    private TransactionLogRepository transactionLogRepository;
    private MockRestServiceServer mockServer;

    private final String requestUri = "https://banking-api.sample.com/transfer";

    @Autowired
    public TransferAccountServiceTest(RestTemplate restTemplate, AccountServiceImpl accountService, AccountRepository accountRepository, TransactionLogRepository transactionLogRepository) {
        this.restTemplate = restTemplate;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("계좌 이체 성공")
    void whenSuccess() throws TemporarilyUnavailableException, NoAccountException, AuthorizationException, BankAPIException {
        //given
        Account account1 = Account.builder().userId(1L).bankCode("D001").bankAccountNumber("1234567890").bankAccountId("12345678").build();
        accountRepository.save(account1);
        Account account2 = Account.builder().userId(2L).bankCode("D003").bankAccountNumber("0987654321").bankAccountId("87654321").build();
        accountRepository.save(account2);

        mockServer
                .expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withStatus(HttpStatus.OK)
                        .body("{\"tx_id\":\"11223344\",\"bank_tx_id\":\"12345678\",\"result\":\"SUCCESS\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        //when
        TransactionLogDto transactionLogDto = accountService.transferAccount(
                1L,
                "D001",
                "1234567890",
                "D003",
                "0987654321",
                "최다윗 송금",
                100000L
        );

        //then
        TransactionLog transactionLog = transactionLogRepository.findByTxId("11223344").orElse(TransactionLog.builder().build());

        Assertions.assertThat(transactionLogDto.getTxId())
                .isEqualTo(transactionLog.getTxId());
        Assertions.assertThat(transactionLogDto.getBankTxId())
                .isEqualTo(transactionLog.getBankTxId());
        Assertions.assertThat(transactionLogDto.getResult())
                .isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("계좌 이체 예외 상황")
    void whenFail() {
        //given
        mockServer
                .expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .body("{\"code\":\"BANKING_ERROR_200\", \"message\":\"등록되지 않은 계좌 ID\"}")
                        .contentType(MediaType.APPLICATION_JSON));
        //when
        try {
            TransactionLogDto transactionLogDto = accountService.transferAccount(
                    1L,
                    "D001",
                    "1234567890",
                    "D003",
                    "0987654321",
                    "최다윗 송금",
                    100000L
            );
        } catch (TemporarilyUnavailableException | NoAccountException | AuthorizationException e) {
            e.printStackTrace();
        } catch (BankAPIException e) {
            //then
            Assertions.assertThat(e.getMessage()).isEqualTo("등록되지 않은 계좌 ID");
        }

    }

}
