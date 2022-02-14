package com.david.bankapplication.global.dto;

import lombok.*;

/**
 * FileName : ErrorResponseDto
 * Author : David
 * Date : 2022-02-15
 * Description : 예외처리를 위한 DTO
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponseDto {

    private String code;
    private String message;

    @Builder
    public ErrorResponseDto(String code, String message){
        this.code = code;
        this.message = message;
    }
}
