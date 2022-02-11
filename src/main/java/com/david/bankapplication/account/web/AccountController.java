package com.david.bankapplication.account.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileName : AccountController
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 등록과 계좌 이체 및 확인 REST API
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@RestController
public class AccountController {
    Logger logger = LoggerFactory.getLogger(AccountController.class);

}
