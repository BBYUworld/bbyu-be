package com.bbyuworld.gagyebbyu.domain.expense.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseMemoCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseTargetCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseUpdateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseDayDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseMonthDto;
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
	 * 커플 지출 월 별 조회
	 * @param param 년, 월, 정렬조건
	 * @return
	 */
	@GetMapping(path = "/month", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<ExpenseMonthDto> getExpenseAll(
		@ModelAttribute ExpenseParam param) {
		return ResponseEntity.ok(expenseService.getExpenseAll(UserContext.getUserId(), param));
	}

	/**
	 * 커플 지출 일 별 조회
	 * @param param 년, 월, 일, 정렬조건
	 * @return
	 */
	@GetMapping(path = "/day", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<List<ExpenseDayDto>> getDayExpense(
		@ModelAttribute ExpenseParam param) {
		return ResponseEntity.ok(expenseService.getDayExpense(UserContext.getUserId(), param));
	}

	/**
	 * 사용자 지출 생성
	 * @param expenseCreateDto
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<Void> createExpense(@RequestBody ExpenseCreateDto expenseCreateDto) {
		expenseService.createExpense(UserContext.getUserId(), expenseCreateDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 사용자 지출 메모
	 * @param expenseId
	 * @param expenseMemoCreateDto
	 * @return
	 */
	@PostMapping(path = "/memo/{expenseId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createExpenseMemo(@PathVariable long expenseId,
		@RequestBody ExpenseMemoCreateDto expenseMemoCreateDto) {
		expenseService.createExpenseMemo(expenseId, expenseMemoCreateDto);
		return ResponseEntity.ok().build();
	}

	/**
	 * 커플 지출 예산 설정
	 * @param expenseTargetCreateDto
	 * @return
	 */
	@PostMapping(path = "/target-amount", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<Void> createExpenseTargetAmount(
		@RequestBody ExpenseTargetCreateDto expenseTargetCreateDto) {
		expenseService.createExpenseTarget(UserContext.getUserId(), expenseTargetCreateDto);
		return ResponseEntity.ok().build();
	}

	/**
	 * 사용자 지출 금액 수정
	 * @param expenseId
	 * @param expenseUpdateDto
	 * @return
	 */
	@PatchMapping(path = "/{expenseId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateExpense(@PathVariable long expenseId,
		@RequestBody ExpenseUpdateDto expenseUpdateDto) {
		expenseService.updateExpense(expenseId, expenseUpdateDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireJwtToken
	public ResponseEntity<ExpenseMonthDto> getTest() {
		System.out.println(expenseService.getUserExpensesForYear(UserContext.getUserId()));
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/")


}
