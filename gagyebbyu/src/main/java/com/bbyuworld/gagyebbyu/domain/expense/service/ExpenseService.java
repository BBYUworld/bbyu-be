package com.bbyuworld.gagyebbyu.domain.expense.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseMemoCreateDto;
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

	public List<ExpenseOverviewDto> getExpenseAll(long userId, ExpenseParam param) {
		Integer month = param.getMonth() != null ? param.getMonth() : LocalDateTime.now().getMonthValue();
		Integer year = param.getYear() != null ? param.getYear() : LocalDateTime.now().getYear();
		String sort = param.getSort() != null ? param.getSort() : "asc";

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		List<Tuple> expenseTuples = expenseRepository.findExpenseByMonth(month, year, user.getCoupleId(), sort);

		List<ExpenseOverviewDto> expenses = new ArrayList<ExpenseOverviewDto>();
		for (Tuple tuple : expenseTuples) {
			Date sqlDate = tuple.get(0, Date.class);
			LocalDate date = sqlDate.toLocalDate();
			Couple couple = tuple.get(1, Couple.class);
			Long amount = tuple.get(2, Long.class);
			expenses.add(new ExpenseOverviewDto(couple.getCoupleId(), date, amount));
		}

		return expenses;
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
		String category = "기타";

		expenseRepository.save(expenseCreateDto.toEntity(user, couple, category));

	}

	@Transactional
	public void createExpenseMemo(long expenseId, ExpenseMemoCreateDto expenseMemoCreateDto) {

		Expense expense = expenseRepository.findById(expenseId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.EXPENSE_NOT_FOUND));

		expense.updateMemo(expenseMemoCreateDto.getMemo());
	}
}
