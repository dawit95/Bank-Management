package com.david.bankapplication.account.service;

import com.david.bankapplication.account.dto.AccountDto;
import com.david.bankapplication.account.dto.ResultDto;
import com.david.bankapplication.account.dto.TransactionLogDto;
import com.david.bankapplication.global.exception.AuthorizationException;
import com.david.bankapplication.global.exception.BankAPIException;
import com.david.bankapplication.global.exception.NoAccountException;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;

/**
 * FileName : AccountService
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 관리에 필요한 service Interface
 */
public interface AccountService {

    //계좌 등록
    AccountDto registerAccount(Long userId, String bankCode) throws TemporarilyUnavailableException, BankAPIException;

    //계좌 이체
    TransactionLogDto transferAccount(Long userId, String fromAccountBankCode, String fromAccountBankNumber, String toAccountBankCode, String toAccountBankNumber, String comment, String transferAmount) throws NoAccountException, AuthorizationException, TemporarilyUnavailableException, BankAPIException;

    //거래 내역 확인
    ResultDto transferInfo(String txId) throws BankAPIException, TemporarilyUnavailableException;

    //util
    String accountGenerator(int numLength);

}
