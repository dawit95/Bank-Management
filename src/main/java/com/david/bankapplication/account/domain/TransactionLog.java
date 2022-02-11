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
    private Long pk;

    @Column(nullable = false, unique = true)
    private Long txId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long fromAccountId;

    @Column(nullable = false)
    private Long toAccountId;

    @Column
    private String comment;

    @Column
    private String amount;

    @Builder
    public TransactionLog(Long userId, Long txId, Long fromAccountId, Long toAccountId, String comment, String amount) {
        this.userId = userId;
        this.txId = txId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.comment = comment;
        this.amount = amount;
    }
}
