package com.david.bankapplication.global.config;

import com.david.bankapplication.global.interceptor.RequestResponseLoggingInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * FileName : RestTemplateConfig
 * Author : David
 * Date : 2022-02-12
 * Description : RestTemplate Bean등록
 */

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate registerRestTemplate() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(7, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(30, 10, TimeUnit.MINUTES)) //커넥션풀 적용
                .build();
        // @formatter:on
        OkHttp3ClientHttpRequestFactory crf = new OkHttp3ClientHttpRequestFactory(client);


        return new RestTemplateBuilder()
                .rootUri("https://banking-api.sample.com")
                .additionalInterceptors(new RequestResponseLoggingInterceptor())
//                .errorHandler(new RestTemplateErrorHandler())
                .requestFactory(() -> crf)
                .build();
    }

}