package com.bbyuworld.gagyebbyu.domain.expense.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseOverviewDto;
import com.bbyuworld.gagyebbyu.domain.expense.service.ExpenseService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;

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
	@RequireJwtToken
	public ResponseEntity<List<ExpenseOverviewDto>> getExpenseAll(
		@ModelAttribute ExpenseParam param) {
		return ResponseEntity.ok(expenseService.getExpenseAll(UserContext.getUserId(), param));
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<Void> createFund(@RequestBody ExpenseCreateDto expenseCreateDto) {
		expenseService.createExpense(UserContext.getUserId(), expenseCreateDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
