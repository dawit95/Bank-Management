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
    private Long fromAccountBankCode;
    private Long fromAccountBankNumber;
    private Long toAccountBankCode;
    private Long toAccountBankNumber;

    private String comment;
    private String amount;
}
