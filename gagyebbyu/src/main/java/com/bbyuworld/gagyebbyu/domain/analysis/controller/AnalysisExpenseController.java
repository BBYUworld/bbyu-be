package com.bbyuworld.gagyebbyu.domain.analysis.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.CoupleExpenseResultDto;
import com.bbyuworld.gagyebbyu.domain.analysis.service.CoupleExpenseService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analysis/couple-expense")
@RequiredArgsConstructor
public class AnalysisExpenseController {
	private final CoupleExpenseService coupleExpenseService;

	@GetMapping(path = "/couple-expense/result", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<CoupleExpenseResultDto> getCoupleExpenseResult() {
		return ResponseEntity.ok(coupleExpenseService.getCoupleExpenseResult(UserContext.getUserId()));
	}

}
