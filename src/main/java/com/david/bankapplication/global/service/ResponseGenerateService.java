package com.david.bankapplication.global.service;

import com.david.bankapplication.global.dto.SuccessResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ResponseGenerateService {

    public SuccessResponseDto generateSuccessResponse(Object data) {
        SuccessResponseDto successResponseDto = SuccessResponseDto.builder().success(data).build();
        return successResponseDto;
    }

}
