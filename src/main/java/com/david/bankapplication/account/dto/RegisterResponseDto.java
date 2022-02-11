package com.david.bankapplication.account.dto;

import lombok.*;

import java.io.Serializable;

/**
 * FileName : RegisterResponseDto
 * Author : David
 * Date : 2022-02-11
 * Description :
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegisterResponseDto implements Serializable {
    private String bank_account_id;
    private String code;
    private String message;

    @Builder
    public RegisterResponseDto(String bank_account_id, String code, String message) {
        this.bank_account_id = bank_account_id;
        this.code = code;
        this.message = message;
    }
}
