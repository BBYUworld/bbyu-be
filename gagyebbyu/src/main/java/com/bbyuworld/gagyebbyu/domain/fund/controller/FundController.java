package com.bbyuworld.gagyebbyu.domain.fund.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.fund.dto.request.FundCreateDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.request.FundTransactionCreateDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundOverViewDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundStatusDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundTransactionDto;
import com.bbyuworld.gagyebbyu.domain.fund.service.FundService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
public class FundController {

	private final FundService fundService;

	/**
	 * 커플 펀딩 조회
	 * @param coupleId
	 * @return
	 */
	@GetMapping(path = "/{coupleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FundOverViewDto> getFund(@PathVariable long coupleId) {
		return ResponseEntity.ok(fundService.getFund(coupleId));
	}

	/**
	 * 커플 펀딩 생성
	 * @param coupleId
	 * @return
	 */
	@PostMapping(path = "/{coupleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<Void> createFund(@PathVariable long coupleId, @RequestBody FundCreateDto fundCreateDto) {
		fundService.createFund(UserContext.getUserId(), coupleId, fundCreateDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 커플 펀딩 삭제
	 * @param fundId
	 * @return
	 */
	@DeleteMapping(path = "/{fundId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteFund(@PathVariable long fundId) {
		fundService.deleteFund(fundId);
		return ResponseEntity.ok().build();
	}

	/**
	 * 펀딩 금액 입출금
	 * @param fundId
	 * @param fundTransactionCreateDto
	 * @return
	 */
	@PostMapping(path = "/transaction/{fundId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<FundStatusDto> createFundTransaction(@PathVariable long fundId,
		@RequestBody FundTransactionCreateDto fundTransactionCreateDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(fundService.createFundTransaction(fundId, UserContext.getUserId(), fundTransactionCreateDto));
	}

	/**
	 * 펀딩 입출금 기록 조회
	 * @param fundId
	 * @return
	 */
	@GetMapping(path = "transaction/{fundId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FundTransactionDto>> getFundTransaction(@PathVariable long fundId) {
		return ResponseEntity.ok(fundService.getFundTransaction(fundId));
	}

}
