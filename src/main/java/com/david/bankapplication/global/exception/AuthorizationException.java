package com.david.bankapplication.global.exception;

/**
 * FileName : AuthorizationException
 * Author : David
 * Date : 2022-02-17
 * Description : 계좌 접근 불가 예외
 */
public class AuthorizationException extends Exception {
    public AuthorizationException(String s) {
        super(s);
    }
}