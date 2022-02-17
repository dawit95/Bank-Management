package com.david.bankapplication.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * FileName : ResultDto
 * Author : David
 * Date : 2022-02-17
 * Description : 외부 API 결과 DTO
 */
@ApiModel("거래 내역 조회 API 성공시 결과") // 모델명
@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ResultDto implements Serializable {
    @ApiModelProperty(notes = "계좌이체 결과 [SUCCESS | FAIL]", example = "1")
    private String result;
}
