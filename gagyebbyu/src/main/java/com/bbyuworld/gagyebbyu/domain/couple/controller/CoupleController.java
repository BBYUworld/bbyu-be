package com.bbyuworld.gagyebbyu.domain.couple.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.couple.dto.request.CoupleCreateDto;
import com.bbyuworld.gagyebbyu.domain.couple.service.CoupleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/couple")
@RequiredArgsConstructor
public class CoupleController {

	private final CoupleService coupleService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createCouple(@RequestBody CoupleCreateDto coupleCreateDto) {
		coupleService.createCouple(coupleCreateDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// @GetMapping(path = "/month", produces = MediaType.APPLICATION_JSON_VALUE)
	// @RequireJwtToken
	// public ResponseEntity<ExpenseMonthDto> getExpenseAll(
	// 	@ModelAttribute ExpenseParam param) {
	// 	return ResponseEntity.ok(expenseService.getExpenseAll(UserContext.getUserId(), param));
	// }
	//
	// /**
	//  * 커플 지출 일 별 조회
	//  * @param param 년, 월, 일, 정렬조건
	//  * @return
	//  */
	// @GetMapping(path = "/day", produces = MediaType.APPLICATION_JSON_VALUE)
	// @RequireJwtToken
	// public ResponseEntity<List<ExpenseDayDto>> getDayExpense(
	// 	@ModelAttribute ExpenseParam param) {
	// 	return ResponseEntity.ok(expenseService.getDayExpense(UserContext.getUserId(), param));
	// }

}