package com.david.bankapplication.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * FileName : RegisterRequestDto
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 등록 요청에 필요한 정보 DTO
 */
@ToString
@Getter
@NoArgsConstructor
public class RegisterRequestDto {
    private Long userId;
    private String bankCode;
    private String amount;
}
