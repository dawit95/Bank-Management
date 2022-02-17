package com.david.bankapplication.account.dto;

import com.david.bankapplication.account.domain.Account;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * FileName : AccountDto
 * Author : David
 * Date : 2022-02-16
 * Description : Account DTO
 */
@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccountDto {
    private Long id;
    private Long userId;
    private String bankCode;
    @ApiModelProperty(value = "등록된 계좌번호")
    private String bankAccountNumber;
    private String bankAccountId;

    public AccountDto(Account entity){
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.bankCode = entity.getBankCode();
        this.bankAccountId = entity.getBankAccountId();
        this.bankAccountNumber = entity.getBankAccountNumber();
    }

}
