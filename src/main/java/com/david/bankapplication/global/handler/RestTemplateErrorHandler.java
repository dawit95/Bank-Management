package com.david.bankapplication.global.handler;

import com.david.bankapplication.global.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * FileName : RestTemplateErrorHandler
 * Author : David
 * Date : 2022-02-15
 * Description : RestTemplate Error Handler
 */
@Slf4j
@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {


    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == CLIENT_ERROR
                || response.getStatusCode().series() == SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        int exceptionStatus = response.getStatusCode().value();
        if(exceptionStatus==400 || exceptionStatus == 422 || exceptionStatus == 500){
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponseDto responseDto = objectMapper.readValue(response.getBody(), ErrorResponseDto.class);
            log.debug("exception code : {}", responseDto.getCode());
            log.debug("exception message : {}", responseDto.getMessage());
        }

    }
}
