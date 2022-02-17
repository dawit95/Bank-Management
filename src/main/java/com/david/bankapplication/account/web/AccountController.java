package com.david.bankapplication.account.web;

import com.david.bankapplication.account.dto.AccountDto;
import com.david.bankapplication.account.dto.RegisterRequestDto;
import com.david.bankapplication.account.dto.TransactionLogDto;
import com.david.bankapplication.account.dto.TransferRequestDto;
import com.david.bankapplication.account.service.AccountServiceImpl;
import com.david.bankapplication.global.dto.SuccessResponseDto;
import com.david.bankapplication.global.exception.AuthorizationException;
import com.david.bankapplication.global.exception.BankAPIException;
import com.david.bankapplication.global.exception.NoAccountException;
import com.david.bankapplication.global.exception.TemporarilyUnavailableException;
import com.david.bankapplication.global.service.ResponseGenerateService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * FileName : AccountController
 * Author : David
 * Date : 2022-02-11
 * Description : 계좌 등록과 계좌 이체 및 확인 REST API
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@RestController
public class AccountController {
    Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountServiceImpl accountService;
    private final ResponseGenerateService responseGenerateService;

    @ApiOperation(value = "계좌등록", notes = "성공시 요청한 은행에 계좌를 생성하고 계좌번호 반환!")
    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDto> register(@RequestBody RegisterRequestDto requestDto) throws TemporarilyUnavailableException, BankAPIException {
        log.debug("AccountController 계좌 등록 in Param : {}", requestDto);

        AccountDto accountDto = accountService.registerAccount(requestDto.getUserId(), requestDto.getBankCode());

        SuccessResponseDto successResponseDto =
                responseGenerateService.generateSuccessResponse(
                        accountDto.getBankAccountNumber());

        return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "계좌이체", notes = "계좌이체 성공시 거래 번호와 결과 반환!")
    @PostMapping("/transfer")
    public ResponseEntity<SuccessResponseDto> transfer(@RequestBody TransferRequestDto requestDto) throws NoAccountException, AuthorizationException, BankAPIException, TemporarilyUnavailableException {
        log.debug("AccountController 계좌 이체 in Param : {}", requestDto);

        TransactionLogDto transactionLogDto = accountService.transferAccount(
                requestDto.getUserId(),
                requestDto.getFromAccountBankCode(),
                requestDto.getFromAccountBankNumber(),
                requestDto.getToAccountBankCode(),
                requestDto.getToAccountBankNumber(),
                requestDto.getComment(),
                requestDto.getTransferAmount()
        );

        SuccessResponseDto successResponseDto =
                responseGenerateService.generateSuccessResponse(transactionLogDto);

        return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "계좌이체", notes = "계좌이체 성공시 거래 번호와 결과 반환!")
    @GetMapping("/transfer/{txId}")
    public ResponseEntity<SuccessResponseDto> transferInfo(@PathVariable String txId) throws BankAPIException, TemporarilyUnavailableException {
        log.debug("AccountController 거래 내역 조회 in Param : {}", txId);

        SuccessResponseDto successResponseDto =
                responseGenerateService.generateSuccessResponse(accountService.transferInfo(txId));

        return new ResponseEntity<>(successResponseDto, HttpStatus.OK);
    }
}
