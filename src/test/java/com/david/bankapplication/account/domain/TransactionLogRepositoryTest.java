package com.david.bankapplication.account.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * FileName : TransactionLogRepositoryTest
 * Author : David
 * Date : 2022-02-17
 * Description : TransactionLogRepository Basics Test (JPA Unit Test)
 */
@DataJpaTest
public class TransactionLogRepositoryTest {
    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Test
    @DisplayName("거래 내역 id가 유일한지 확인")
    void duplicatedTxId(){
        //given
        TransactionLog transactionLog = TransactionLog.builder()
                .userId(1L)
                .txId("11223344")
                .bankTxId("12345678")
                .fromAccountId(1L)
                .toAccountId(2L)
                .comment("입금")
                .transferAmount("100,000")
                .build();
        transactionLogRepository.save(transactionLog);

        //when
        boolean result1 = transactionLogRepository.existsByTxId("11223344");
        boolean result2 = transactionLogRepository.existsByTxId("22334455");
        boolean result3 = transactionLogRepository.existsByTxId("");

        //then
        Assertions.assertThat(result1).isTrue();
        Assertions.assertThat(result2).isFalse();
        Assertions.assertThat(result3).isFalse();
    }
}
