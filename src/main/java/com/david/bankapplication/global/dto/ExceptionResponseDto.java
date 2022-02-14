package com.david.bankapplication.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * FileName : ExceptionResponseDto
 * Author : David
 * Date : 2022-02-15
 * Description : 예외시 응답 형식
 */
@NoArgsConstructor
@Getter
public class ExceptionResponseDto {
    private Object error;

    @Builder
    public ExceptionResponseDto(Object error) {
        this.error = error;
    }
}
