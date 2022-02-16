package com.david.bankapplication.global.exception;

/**
 * FileName : BankAPIException
 * Author : David
 * Date : 2022-02-17
 * Description : 외부 API 예외 중
 */
public class BankAPIException extends Exception {
    public BankAPIException(String s) {
        super(s);
    }
}
