package com.david.bankapplication.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * FileName : TransferRequestDto
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌이체 요청에 필요한 정보 DTO
 */
@ApiModel("계좌이체 RequestParam 객체 도메인")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransferRequestDto {
    @ApiModelProperty(value = "회원 고유 번호", required = true, example = "1")
    private Long userId;

    @ApiModelProperty(value = "송금할 회원의 계좌 은행코드", required = true, example = "D001")
    private String fromAccountBankCode;
    @ApiModelProperty(value = "송금할 회원의 계좌 번호", required = true, example = "0123456789")
    private String fromAccountBankNumber;
    @ApiModelProperty(value = "입금될 계좌 은행코드", required = true, example = "D002")
    private String toAccountBankCode;
    @ApiModelProperty(value = "입금될 계좌 번호", required = true, example = "9876543210")
    private String toAccountBankNumber;

    @ApiModelProperty(value = "계좌이체 Comment", required = true, example = "다윗님 입사 축하금!")
    private String comment;
    @ApiModelProperty(value = "입금 금액", required = true, example = "5,000,000")
    private Long transferAmount;
}
