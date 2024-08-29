package com.bbyuworld.gagyebbyu.domain.recommend.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.MoneyDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendDepositDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendLoanDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendResponseDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendSavingsDto;
import com.bbyuworld.gagyebbyu.domain.recommend.service.RecommendService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {

	private final RecommendService recommendService;

	/**
	 * 대출 추천 api
	 *
	 * @return
	 */
	@PostMapping(path = "/loan", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<List<RecommendLoanDto>> getLoanRecommend() {
		return ResponseEntity.ok(recommendService.getLoanRecommend(UserContext.getUserId()));
	}

	/**
	 * 예금 추천 api
	 * @return
	 */
	@PostMapping(path = "/deposit", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<List<RecommendDepositDto>> getDepositRecommend() {
		return ResponseEntity.ok(recommendService.getDepositRecommend(UserContext.getUserId()));
	}

	/**
	 * 적금 추천 api
	 * @return
	 */
	@PostMapping(path = "/savings", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<List<RecommendSavingsDto>> getSavingsRecommend() {
		return ResponseEntity.ok(recommendService.getSavingsRecommend(UserContext.getUserId()));
	}

	/**
	 * 남녀 공동 비율 및 최적 대출 상품 api
	 * @return
	 */
	@PostMapping(path = "/compare", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<RecommendResponseDto> getCompareRecommend(@RequestBody MoneyDto moneyDto) {
		return ResponseEntity.ok(recommendService.getCompareRecommend(UserContext.getUserId(), moneyDto.getMoney()));
	}

}
