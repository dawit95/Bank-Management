package com.david.bankapplication.global.exception;

/**
 * FileName : NoAccountException
 * Author : David
 * Date : 2022-02-17
 * Description : 없는 계좌 요청
 */
public class NoAccountException extends Exception {
    public NoAccountException(String s) {
        super(s);
    }
}