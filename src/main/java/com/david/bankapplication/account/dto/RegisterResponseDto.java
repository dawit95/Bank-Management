package com.david.bankapplication.account.dto;

import lombok.*;

import java.io.Serializable;

/**
 * FileName : RegisterResponseDto
 * Author : David
 * Date : 2022-02-11
 * Description : 외부 API 결과 DTO
 */
@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RegisterResponseDto implements Serializable {
    private String bank_account_id;

}
