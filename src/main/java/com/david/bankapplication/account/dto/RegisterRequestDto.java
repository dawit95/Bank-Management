package com.david.bankapplication.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * FileName : RegisterRequestDto
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 등록 요청에 필요한 정보 DTO
 */
@ApiModel("계좌 등록 RequestParam 객체 도메인")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RegisterRequestDto {

    @ApiModelProperty(value = "회원 고유 번호", required = true, example = "1")
    private Long userId;

    @ApiModelProperty(value = "은행 코드 [\"D001\", \"D002\", \"D003\"]", required = true, example = "D001")
    private String bankCode;
}
