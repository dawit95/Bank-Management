package com.david.bankapplication.global.exception;

/**
 * FileName : RetryException
 * Author : David
 * Date : 2022-02-15
 * Description : 다시 시도를 위한 예외
 */
public class RetryException extends Exception{
    public RetryException(String s) { super(s);}
}
