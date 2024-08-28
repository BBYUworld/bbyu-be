package com.bbyuworld.gagyebbyu.domain.recommend.controller;

import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendDepositDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendSavingsDto;
import com.bbyuworld.gagyebbyu.domain.recommend.service.RecommendService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
	public ResponseEntity<List<Map.Entry<Integer, Double>>> getLoanRecommend() {
		return ResponseEntity.ok(recommendService.getLoanRecommend(UserContext.getUserId()));

	}

	/**
	 * 예금 추천 api
	 * @return
	 */
	@PostMapping(path = "/deposit", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<List<Map.Entry<Integer, Double>>> getDepositRecommend() {
		return ResponseEntity.ok(recommendService.getDepositRecommend(UserContext.getUserId()));
	}

	/**
	 * 적금 추천 api
	 * @return
	 */
	@PostMapping(path = "/savings", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<List<Map.Entry<Integer, Double>>> getSavingsRecommend() {
		return ResponseEntity.ok(recommendService.getSavingsRecommend(UserContext.getUserId()));
	}


}
