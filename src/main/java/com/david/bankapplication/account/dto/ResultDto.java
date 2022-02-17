package com.david.bankapplication.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * FileName : ResultDto
 * Author : David
 * Date : 2022-02-17
 * Description : 외부 API 결과 DTO
 */
@ToString
@Getter @Setter
@NoArgsConstructor
public class ResultDto implements Serializable {
    private String result;
}
