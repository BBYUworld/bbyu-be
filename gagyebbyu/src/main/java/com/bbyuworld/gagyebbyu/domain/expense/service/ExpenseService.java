package com.bbyuworld.gagyebbyu.domain.expense.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseMemoCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseTargetCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseUpdateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseDayDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseMonthDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseOverviewDto;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;
import com.bbyuworld.gagyebbyu.domain.expense.repository.ExpenseRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import com.querydsl.core.Tuple;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final UserRepository userRepository;
	private final CoupleRepository coupleRepository;

	public ExpenseMonthDto getExpenseAll(long userId, ExpenseParam param) {
		Integer month = param.getMonth() != null ? param.getMonth() : LocalDateTime.now().getMonthValue();
		Integer year = param.getYear() != null ? param.getYear() : LocalDateTime.now().getYear();
		String sort = param.getSort() != null ? param.getSort() : "asc";

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
		System.out.println("user id = "+user.getUserId());
		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));
		System.out.println("couple id = "+couple.getCoupleId());
		List<Tuple> expenseTuples = expenseRepository.findExpenseByMonth(month, year, user.getCoupleId(), sort);

		long totalAmount = 0;
		long targetAmount = couple.getMonthlyTargetAmount();

		List<ExpenseOverviewDto> expenses = new ArrayList<ExpenseOverviewDto>();
		for (Tuple tuple : expenseTuples) {
			Date sqlDate = tuple.get(0, Date.class);
			LocalDate date = sqlDate.toLocalDate();
			Long amount = tuple.get(2, Long.class);
			totalAmount += amount;
			expenses.add(new ExpenseOverviewDto(couple.getCoupleId(), date, amount));
		}

		return new ExpenseMonthDto(totalAmount, targetAmount,
			targetAmount - totalAmount, expenses);
	}

	public List<ExpenseDayDto> getDayExpense(long userId, ExpenseParam param) {
		Integer day = param.getDay() != null ? param.getDay() : LocalDateTime.now().getDayOfMonth();
		Integer month = param.getMonth() != null ? param.getMonth() : LocalDateTime.now().getMonthValue();
		Integer year = param.getYear() != null ? param.getYear() : LocalDateTime.now().getYear();
		String sort = param.getSort() != null ? param.getSort() : "asc";

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		return expenseRepository.findExpenseByDay(day, month, year, user.getCoupleId(), sort)
			.stream()
			.map(ExpenseDayDto::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public void createExpense(long userId, ExpenseCreateDto expenseCreateDto) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = null;

		if (user.getCoupleId() != null) {
			couple = coupleRepository.findById(user.getCoupleId())
				.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));
		}

		//카테고리 가져오는 로직 (추후 수정)
		/**
		 * 교육,
		 *     교통_자동차,
		 *     기타소비,
		 *     대형마트,
		 *     미용,
		 *     배달,
		 *     보험,
		 *     생필품,
		 *     생활서비스,
		 *     세금_공과금,
		 *     쇼핑몰,
		 *     여행_숙박,
		 *     외식,
		 *     의료_건강,
		 *     주류_펍,
		 *     취미_여가,
		 *     카페,
		 *     통신,
		 *     편의점
		 *     이거 중에 하나 String 값으로 보내면 자동으로 ENUM 처리돼서 저장됨
		 */
		String category = "통신";

		expenseRepository.save(expenseCreateDto.toEntity(user, couple, category));

	}

	@Transactional
	public void createExpenseMemo(long expenseId, ExpenseMemoCreateDto expenseMemoCreateDto) {

		Expense expense = expenseRepository.findById(expenseId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.EXPENSE_NOT_FOUND));

		expense.updateMemo(expenseMemoCreateDto.getMemo());
	}

	@Transactional
	public void createExpenseTarget(long userId, ExpenseTargetCreateDto expenseTargetCreateDto) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		couple.updateTargetAmount(expenseTargetCreateDto.getTargetAmount());
	}

	@Transactional
	public void updateExpense(long expenseId, ExpenseUpdateDto expenseUpdateDto) {

		Expense expense = expenseRepository.findById(expenseId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.EXPENSE_NOT_FOUND));

		expense.updateAmount(expenseUpdateDto.getAmount());
	}
}
