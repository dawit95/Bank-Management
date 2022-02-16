package com.david.bankapplication.account.domain;

import com.david.bankapplication.global.util.BaseTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * FileName : Account
 * Author : David
 * Date : 2022-02-11
 * Description : 사용자가 등록한 계좌 DB
 */
@Getter
@NoArgsConstructor
@Entity
public class Account extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String bankCode;

    @Column(nullable = false)
    private String bankAccountNumber;

    @Column
    private String bankAccountId;

    @Column
    private String amount;

    @Builder
    public Account(Long userId, String bankCode, String bankAccountNumber, String bankAccountId, String amount) {
        this.userId = userId;
        this.bankCode = bankCode;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }

    public void updateBankAccount(String bankAccountId, String bankAccountNumber){
        this.bankAccountId = bankAccountId;
        this.bankAccountNumber = bankAccountNumber;
    }

    public void updateAmount(String amount){
        this.amount = amount;
    }

}
