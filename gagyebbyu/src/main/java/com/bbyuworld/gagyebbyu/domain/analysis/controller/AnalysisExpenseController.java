package com.bbyuworld.gagyebbyu.domain.analysis.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.CoupleExpenseResultDto;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.CoupleExpenseStatisticsDto;
import com.bbyuworld.gagyebbyu.domain.analysis.service.CoupleExpenseService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analysis/couple-expense")
@RequiredArgsConstructor
public class AnalysisExpenseController {
	private final CoupleExpenseService coupleExpenseService;

	/**
	 * 최종 소비 패턴 분석(최다 소비 카테고리, 타 부부 평균 소비량(연령, 나이에 맞는), 우리 부부 소비량, 우리 커플 평균 나이
	 * @return
	 */
	@GetMapping(path = "/result", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<CoupleExpenseResultDto> getCoupleExpenseResult() {
		return ResponseEntity.ok(coupleExpenseService.getCoupleExpenseResult(UserContext.getUserId()));
	}

	/**
	 * 소비 카테고리 별 통계
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<List<CoupleExpenseStatisticsDto>> getCoupleExpenseStatistics() {
		return ResponseEntity.ok(coupleExpenseService.getCoupleExpenseStatistics(UserContext.getUserId()));
	}

}
