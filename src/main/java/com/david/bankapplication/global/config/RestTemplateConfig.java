package com.david.bankapplication.global.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
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
    public RestTemplate bankRestTemplate() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(7, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(30, 10, TimeUnit.MINUTES)) //커넥션풀 적용
                .build();
        // @formatter:on
        OkHttp3ClientHttpRequestFactory crf = new OkHttp3ClientHttpRequestFactory(client);


        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .requestFactory(() -> crf)
                .build();
    }

}

//@Configuration
//public class RestTemplateConfig {
//
//    @Bean
//    public RestTemplate bankRestTemplate(){
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setMaxConnTotal(200)
//                .setMaxConnPerRoute(20)
//                .build();
//        httpRequestFactory.setHttpClient(httpClient);
//        RestTemplate restTemplate  = new RestTemplateBuilder()
//                .setConnectTimeout(Duration.ofSeconds(5))
//                .setReadTimeout(Duration.ofSeconds(5))
//                .requestFactory(() -> httpRequestFactory)
//                .build();
//        return restTemplate;
//
//    }
//}
