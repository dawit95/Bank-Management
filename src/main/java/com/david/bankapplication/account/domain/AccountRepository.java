package com.david.bankapplication.account.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * FileName : AccountRepository
 * Author : David
 * Date : 2022-02-11
 * Description : Account Entity DB Layer accessor
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Boolean existsByBankCodeAndBankAccountNumber(String bankCode, String bankAccountNumber);
    Optional<Account> findByBankCodeAndBankAccountNumber(String bankCode, String bankAccountNumber);
    Optional<Account> findByBankAccountId(String bankAccountId);
    @NotNull
    Optional<Account> findById(@NotNull Long id);
}
