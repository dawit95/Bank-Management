package com.david.bankapplication.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SuccessResponseDto {
    private Object success;

    @Builder
    public SuccessResponseDto(Object success) {
        this.success = success;
    }
}
