package com.david.bankapplication.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * FileName : TransferRequestDto
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌이체 요청에 필요한 정보 DTO
 */
@ToString
@Getter
@NoArgsConstructor
public class TransferRequestDto {
    private Long userId;

    private String fromAccountBankCode;
    private String fromAccountBankNumber;
    private String toAccountBankCode;
    private String toAccountBankNumber;

    private String comment;
    private String transfer_amount;
}
