package com.david.bankapplication.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * FileName : TransactionLogRepository
 * Author : David
 * Date : 2022-02-11
 * Description : TransactionLog Entity DB Layer accessor
 */
@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

    Boolean existsByTxId(String txId);
    Optional<TransactionLog> findByTxId(String txId);
}
