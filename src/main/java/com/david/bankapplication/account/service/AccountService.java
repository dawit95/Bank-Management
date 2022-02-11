package com.david.bankapplication.account.service;

/**
 * FileName : AccountService
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 관리에 필요한 service Interface
 */
public interface AccountService {

    //계좌 등록
    String registerAccount(Long userId, String bankCode);

    //계좌 이체
    String transferAccount(Long userId, String txId, Long fromAccountId, Long toAccountId, String comment, String amount);

    //util
    String accountGenerator();

}
