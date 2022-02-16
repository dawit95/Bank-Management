package com.david.bankapplication.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * FileName : RequestResponseLoggingInterceptor
 * Author : David
 * Date : 2022-02-15
 * Description : RestTemplate의 요청과 결과 log찍어보기
 */

@Slf4j
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    @NotNull
    @Override
    public ClientHttpResponse intercept(@NotNull HttpRequest request, @NotNull byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.debug(
                "===========================request begin================================================");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("Headers     : {}", request.getHeaders());
        log.debug("Request body: {}", new String(body, Charset.defaultCharset()));
        log.debug(
                "==========================request end================================================");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        log.debug(
                "============================response begin==========================================");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Status text  : {}", response.getStatusText());
        log.debug("Headers      : {}", response.getHeaders());
        // InputStream 에서 모두 읽혀 code error를 이르킴
//        log.debug("Response body: {}",
//                StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        log.debug(
                "=======================response end=================================================");
    }
}
