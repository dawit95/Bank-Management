package com.david.bankapplication.account.service;

import com.david.bankapplication.account.dto.AccountDto;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;

/**
 * FileName : AccountService
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 관리에 필요한 service Interface
 */
public interface AccountService {

    //계좌 등록
    AccountDto registerAccount(Long userId, String bankCode, String amount) throws TemporarilyUnavailableException;

    //계좌 이체
    String transferAccount(Long userId, String txId, Long fromAccountId, Long toAccountId, String comment, String amount);

    //util
    String accountGenerator();

}
