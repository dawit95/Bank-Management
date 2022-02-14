package com.david.bankapplication.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * FileName : AccountRepository
 * Author : David
 * Date : 2022-02-11
 * Description : Account Entity DB Layer accessor
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Boolean existsByBankCodeAndBankAccountNumber(String bankCode, String bankAccountNumber);
}
