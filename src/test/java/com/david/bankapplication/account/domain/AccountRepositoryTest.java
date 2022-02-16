package com.david.bankapplication.account.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * FileName : AccountRepositoryTest
 * Author : David
 * Date : 2022-02-14
 * Description : AccountRepository Basics Test (JPA Unit Test)
 */

@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("은행과 계좌번호 둘다 동일한 계좌가 있는지 확인")
    void duplicatedAccount(){
        //given
        Account account = Account.builder().userId(1L).bankCode("D001").bankAccountNumber("1234567890").bankAccountId("12345678").amount("100,000").build();
        accountRepository.save(account);

        //when
        boolean result1 = accountRepository.existsByBankCodeAndBankAccountNumber(account.getBankCode(),account.getBankAccountNumber());
        boolean result2 = accountRepository.existsByBankCodeAndBankAccountNumber("D002",account.getBankAccountNumber());
        boolean result3 = accountRepository.existsByBankCodeAndBankAccountNumber(account.getBankCode(),"0123456789");
        boolean result4 = accountRepository.existsByBankCodeAndBankAccountNumber("D002","0123456789");
        boolean result5 = accountRepository.existsByBankCodeAndBankAccountNumber("","");

        //then
        Assertions.assertThat(result1).isTrue();
        Assertions.assertThat(result2).isFalse();
        Assertions.assertThat(result3).isFalse();
        Assertions.assertThat(result4).isFalse();
        Assertions.assertThat(result5).isFalse();
    }

    @Test
    @DisplayName("은행과 계좌번호의 해당 계좌 가져오기")
    void findAccount(){
        //given
        Account originAccount = Account.builder().userId(1L).bankCode("D001").bankAccountNumber("1234567890").bankAccountId("12345678").amount("100,000").build();
        accountRepository.save(originAccount);

        //when
        Account account1 = accountRepository.findByBankCodeAndBankAccountNumber(originAccount.getBankCode(),originAccount.getBankAccountNumber()).orElse(null);
        Account account2 = accountRepository.findByBankCodeAndBankAccountNumber("D002",originAccount.getBankAccountNumber()).orElse(null);

        //then
        Assertions.assertThat(originAccount).isEqualTo(account1);
        Assertions.assertThat(account2).isNull();
    }

    @Test
    @DisplayName("계좌 PK에 해당 계좌 가져오기")
    void findAccountById(){
        //given
        Account oneAccount = Account.builder().userId(1L).bankCode("D001").bankAccountNumber("1234567890").bankAccountId("12345678").amount("100,000").build();
        accountRepository.save(oneAccount);
        Long targetId = oneAccount.getId();
        Account twoAccount = Account.builder().userId(2L).bankCode("D001").bankAccountNumber("0123456789").bankAccountId("12345679").amount("100,000").build();
        accountRepository.save(twoAccount);

        //when
        Account account = accountRepository.findById(targetId).orElse(null);

        //then
        Assertions.assertThat(oneAccount).isEqualTo(account);
        Assertions.assertThat(twoAccount).isNotEqualTo(account);
    }
}
