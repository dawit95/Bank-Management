package com.david.bankapplication.account.dto;

import com.david.bankapplication.account.domain.TransactionLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * FileName : TransactionLog
 * Author : David
 * Date : 2022-02-17
 * Description :TransactionLog DTO
 */
@ApiModel("계좌이체 API 성공시 결과")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionLogDto {
    @ApiModelProperty(notes = "거래 고유 값 (8자리)", example = "11223344")
    private String txId;
    @ApiModelProperty(notes = "외부 API 고유 값 (8자리)", example = "12345678")
    private String bankTxId;
    @ApiModelProperty(notes = "계좌이체 결과 [SUCCESS | FAIL]", example = "1")
    private String result;

    public TransactionLogDto(TransactionLog entity){
        this.txId = entity.getTxId();
        this.bankTxId = entity.getBankTxId();
    }
}
