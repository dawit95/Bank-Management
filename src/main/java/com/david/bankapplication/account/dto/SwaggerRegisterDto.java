package com.david.bankapplication.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * FileName : SwaggerRegisterDto
 * Author : David
 * Date : 2022-02-17
 * Description : 명세서에 들어갈 계좌등록 성공 Json
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel("계좌 등록 API 성공시 결과") // 모델명
public class SwaggerRegisterDto {
    @ApiModelProperty(notes = "등록된 계좌번호 (10자리)", example = "0123456789")
    private String accountNumber;
}
