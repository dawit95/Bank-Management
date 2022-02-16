package com.david.bankapplication.global.handler;

import com.david.bankapplication.global.dto.ExceptionResponseDto;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;
import com.david.bankapplication.global.service.ResponseGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * FileName : ExceptionController
 * Author : David
 * Date : 2022-02-15
 * Description : ExceptionController
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ExceptionController {
    private final ResponseGenerateService responseGenerateService;

    @ExceptionHandler(TemporarilyUnavailableException.class)
    protected ResponseEntity<ExceptionResponseDto> handleEmptyResultDataAccess(TemporarilyUnavailableException e) {
        log.error("[TemporarilyUnavailableException] ", e);

        ExceptionResponseDto exceptionResponseDto = responseGenerateService.generateExceptionResponse(e.getMessage().isBlank() ? "일시적인 문제가 생겼습니다. 잠시후 다시 시도해주세요!" : e.getMessage());

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
