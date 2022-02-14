package com.david.bankapplication.global.service;

import com.david.bankapplication.global.dto.ExceptionResponseDto;
import com.david.bankapplication.global.dto.SuccessResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ResponseGenerateService {

    public ExceptionResponseDto generateExceptionResponse(String message) {
        return ExceptionResponseDto.builder().error(message).build();
    }

    public SuccessResponseDto generateSuccessResponse(Object data) {
        return SuccessResponseDto.builder().success(data).build();
    }

}
