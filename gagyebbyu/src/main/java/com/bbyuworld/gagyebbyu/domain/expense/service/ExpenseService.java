package com.bbyuworld.gagyebbyu.domain.expense.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.CategoryDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseMemoCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseTargetCreateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.request.ExpenseUpdateDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseDayDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseMonthDto;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseOverviewDto;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;
import com.bbyuworld.gagyebbyu.domain.expense.repository.ExpenseRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.domain.webClient.dto.ExpenseCategoryDto;
import com.bbyuworld.gagyebbyu.domain.webClient.service.ApiService;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.BadRequestException;
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
	private final ApiService apiService;

	public ExpenseMonthDto getExpenseAll(long userId, ExpenseParam param) {
		Integer month = param.getMonth() != null ? param.getMonth() : LocalDateTime.now().getMonthValue();
		Integer year = param.getYear() != null ? param.getYear() : LocalDateTime.now().getYear();
		String sort = param.getSort() != null ? param.getSort() : "asc";

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		List<Tuple> expenseTuples = expenseRepository.findExpenseByMonth(month, year, user.getCoupleId(), sort);

		long totalAmount = 0;
		long targetAmount = couple.getMonthlyTargetAmount();

		List<ExpenseOverviewDto> expenses = new ArrayList<>();
		for (Tuple tuple : expenseTuples) {
			LocalDateTime dateTime = tuple.get(0, LocalDateTime.class);
			LocalDate date = dateTime.toLocalDate();
			Long amount = tuple.get(2, Long.class);
			totalAmount += amount;
			expenses.add(new ExpenseOverviewDto(couple.getCoupleId(), date, amount));
		}
		System.out.println("expenses size = " + expenses.size());

		List<ExpenseDayDto> dayExpenses = expenseRepository.findExpenseByDay(null, month, year, user.getCoupleId(),
				sort)
			.stream()
			.map(ExpenseDayDto::from)
			.collect(Collectors.toList());

		LocalDateTime startDate = LocalDateTime.of(LocalDate.now().getYear(), month - 1, 1, 0, 0);
		LocalDateTime endDate = startDate.plusMonths(1).minusNanos(1);

		Long totalAmountForLastMonth = expenseRepository.findTotalExpenditureForCoupleGivenMonth(couple.getCoupleId(),
			startDate, endDate);
		totalAmountForLastMonth = totalAmountForLastMonth != null ? totalAmountForLastMonth : 0L;

		Category category = expenseRepository.findTopCategoryForCoupleLastMonth(couple.getCoupleId(), month, year);

		return new ExpenseMonthDto(totalAmount, targetAmount,
			targetAmount - totalAmount, category, totalAmountForLastMonth - totalAmount, expenses, dayExpenses);
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

		ExpenseCategoryDto requestBody = new ExpenseCategoryDto(expenseCreateDto.getPlace());
		try {
			String category = apiService.sendPostRequest("http://3.39.19.140:8000/ai/expense-category", requestBody);
			expenseRepository.save(expenseCreateDto.toEntity(user, couple, category));
		} catch (Exception e) {
			throw new RuntimeException("Failed to create expense", e);
		}
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

	@Transactional
	public void updateExpenseCategory(long expenseId, CategoryDto categoryDto) {

		Expense expense = expenseRepository.findById(expenseId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.EXPENSE_NOT_FOUND));

		expense.updateCategory(categoryDto.getCategory());
	}



	public Long getUserExpensesForYear(long userId) {
		Long totalExpenseForYear = expenseRepository.findTotalExpenditureForYear(userId);
		return totalExpenseForYear != null ? totalExpenseForYear : 0L;
	}

	public List<ExpenseDayDto> getExpenseForMonthAndCategory(long userId, ExpenseParam param, CategoryDto categoryDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		Integer month = param.getMonth() != null ? param.getMonth() : LocalDateTime.now().getMonthValue();
		Integer year = param.getYear() != null ? param.getYear() : LocalDateTime.now().getYear();

		if (categoryDto.getCategory() == null) {
			throw new BadRequestException(ErrorCode.CATEGORY_NOT_FOUND);
		}

		return expenseRepository.getExpenseForMonthAndCategory(couple.getCoupleId(), categoryDto.getCategory(), month,
				year)
			.stream()
			.map(ExpenseDayDto::from)
			.collect(Collectors.toList());

	}
}
