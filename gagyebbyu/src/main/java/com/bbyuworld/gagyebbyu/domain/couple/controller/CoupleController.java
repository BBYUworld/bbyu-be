package com.bbyuworld.gagyebbyu.domain.couple.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.couple.dto.request.CoupleCreateDto;
import com.bbyuworld.gagyebbyu.domain.couple.dto.request.CoupleUpdateDto;
import com.bbyuworld.gagyebbyu.domain.couple.dto.response.CoupleResponseDto;
import com.bbyuworld.gagyebbyu.domain.couple.service.CoupleService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/couple")
@RequiredArgsConstructor
public class CoupleController {

	private final CoupleService coupleService;

	/**
	 * 커플 등록
	 * @param coupleCreateDto
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createCouple(@RequestBody CoupleCreateDto coupleCreateDto) {
		coupleService.createCouple(coupleCreateDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 커플 프로필 수정
	 * @param coupleUpdateDto
	 * @return
	 */
	@PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<Void> updateCouple(
		@RequestBody CoupleUpdateDto coupleUpdateDto) {
		coupleService.updateCouple(UserContext.getUserId(), coupleUpdateDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<CoupleResponseDto> updateCouple() {
		return ResponseEntity.ok(coupleService.getCouple(UserContext.getUserId()));
	}

}