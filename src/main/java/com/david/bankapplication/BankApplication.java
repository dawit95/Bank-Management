package com.david.bankapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class BankApplication {

    @PostConstruct
    public void started() {
        // timezone Asia/Seoul 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));

    }

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
