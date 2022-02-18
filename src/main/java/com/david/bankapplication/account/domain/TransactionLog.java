package com.david.bankapplication.account.domain;

import com.david.bankapplication.global.util.BaseTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * FileName : TransactionLog
 * Author : David
 * Date : 2022-02-11
 * Description : 거래내역 기록 DB
 */
@Getter
@NoArgsConstructor
@Entity
public class TransactionLog extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String txId;

    @Column(nullable = false)
    private String bankTxId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long fromAccountId;

    @Column(nullable = false)
    private Long toAccountId;

    @Column
    private String comment;

    @Column
    private Long transferAmount;

    @Builder
    public TransactionLog(Long userId, String txId,String bankTxId, Long fromAccountId, Long toAccountId, String comment, Long transferAmount) {
        this.userId = userId;
        this.txId = txId;
        this.bankTxId = bankTxId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.comment = comment;
        this.transferAmount = transferAmount;
    }
}
