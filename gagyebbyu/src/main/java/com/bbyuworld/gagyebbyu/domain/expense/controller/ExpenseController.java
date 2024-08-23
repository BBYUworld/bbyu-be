package com.bbyuworld.gagyebbyu.domain.expense.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseOverviewDto;
import com.bbyuworld.gagyebbyu.domain.expense.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

	private final ExpenseService expenseService;

	/**
	 *
	 * @param param
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ExpenseOverviewDto>> getExpenseAll(
		@ModelAttribute ExpenseParam param) {
		long userId = 1;
		return ResponseEntity.ok(expenseService.getExpenseAll(userId, param));
	}

	// /**
	//  * 커플 펀딩 생성
	//  * @param coupleId
	//  * @return
	//  */
	// @PostMapping(path = "/{coupleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<Void> createFund(@PathVariable long coupleId, @RequestBody FundCreateDto fundCreateDto) {
	// 	fundService.createFund(coupleId, fundCreateDto);
	// 	return ResponseEntity.status(HttpStatus.CREATED).build();
	// }

}
