package com.david.bankapplication.global.exception;

/**
 * FileName : TemporarilyUnavailableException
 * Author : David
 * Date : 2022-02-15
 * Description : 잠시 외부 서버와 현 서버의 문제로 정상적인 응답을 줄수 없을때.
 */
public class TemporarilyUnavailableException extends Exception{
    public TemporarilyUnavailableException(String s) { super(s);}
}
