package com.david.bankapplication.account.dto;

import com.david.bankapplication.account.domain.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * FileName : AccountDto
 * Author : David
 * Date : 2022-02-16
 * Description : Account DTO
 */
@ToString
@Getter @Setter
@NoArgsConstructor
public class AccountDto {
    private Long pk;
    private Long userId;
    private String bankCode;
    private String bankAccountNumber;
    private String bankAccountId;

    public AccountDto(Account entity){
        this.pk = entity.getPk();
        this.userId = entity.getUserId();
        this.bankCode = entity.getBankCode();
        this.bankAccountId = entity.getBankAccountId();
        this.bankAccountNumber = entity.getBankAccountNumber();
    }

}
