package com.david.bankapplication.account.dto;

import com.david.bankapplication.account.domain.TransactionLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * FileName : TransactionLog
 * Author : David
 * Date : 2022-02-17
 * Description :TransactionLog DTO
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class TransactionLogDto {
    private String txId;
    private String bankTxId;
    private String result;

    public TransactionLogDto(TransactionLog entity){
        this.txId = entity.getTxId();
        this.bankTxId = entity.getBankTxId();
    }
}
