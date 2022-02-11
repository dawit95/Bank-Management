package com.david.bankapplication.global.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SuccessResponseDto {
    private Object success;

    @Builder
    public SuccessResponseDto(Object success) {
        this.success = success;
    }
}
