package com.david.bankapplication.global.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * FileName : RestTemplateUtil
 * Author : David
 * Date : 2022-02-12
 * Description : RestTemplate Util
 */
@Component
public class RestTemplateUtil {
    private static final String url = "https://banking-api.sample.com";
    private static RestTemplate restTemplate;

    @Autowired
    public RestTemplateUtil(RestTemplate restTemplate) {
        RestTemplateUtil.restTemplate =restTemplate;
    }

    public static ResponseEntity<String> getAccountResponseEntity(HttpEntity httpEntity){
        return restTemplate.exchange(
                url+"/register",
                HttpMethod.POST,
                httpEntity,
                String.class);
    }
}
