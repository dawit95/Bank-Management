package com.david.bankapplication.account.dto;

import lombok.*;

import java.io.Serializable;

/**
 * FileName : TransferResponseDto
 * Author : David
 * Date : 2022-02-17
 * Description : 외부 API 결과 DTO
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransferResponseDto implements Serializable {

    private String tx_id;
    private String bank_tx_id;
    private String result;
}
