package com.david.bankapplication.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * FileName : SuccessResponseDto
 * Author : David
 * Date : 2022-02-15
 * Description : 성공시 응답 형식
 */
@NoArgsConstructor
@Getter
public class SuccessResponseDto {
    private Object success;

    @Builder
    public SuccessResponseDto(Object success) {
        this.success = success;
    }
}
