package com.bbyuworld.gagyebbyu.domain.analysis.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.param.AnalysisParam;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.CoupleExpenseResultDto;
import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.CoupleExpenseStatisticsDto;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;
import com.bbyuworld.gagyebbyu.domain.expense.repository.ExpenseRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoupleExpenseService {
	private final UserRepository userRepository;
	private final CoupleRepository coupleRepository;
	private final ExpenseRepository expenseRepository;

	public CoupleExpenseResultDto getCoupleExpenseResult(long userId, AnalysisParam param) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		int avgAge = (couple.getUser1().getAge() + couple.getUser2().getAge()) / 2;

		int startAge = avgAge / 10 * 10;
		int endAge = startAge + 9;

		long avgIncome = (couple.getUser1().getMonthlyIncome() + couple.getUser2().getMonthlyIncome()) / 2;

		long startIncome = avgIncome / 100000 * 100000 - 1000000;
		long endIncome = startIncome + 1000000;

		double anotherCoupleMonthExpenseAvg = expenseRepository.findAverageExpenditureForEligibleCouples(
			startAge, endAge, startIncome, endIncome);

		Integer month = param.getMonth() == null ? LocalDateTime.now().getMonthValue() : param.getMonth();
		Integer year = param.getYear() == null ? LocalDateTime.now().getYear() : param.getYear();

		Category category = expenseRepository.findTopCategoryForCoupleLastMonth(couple.getCoupleId(), month, year);

		long coupleMonthExpense = expenseRepository.findTotalExpenditureForMonth(couple.getCoupleId(), month, year);

		return new CoupleExpenseResultDto(category, startAge, startIncome + 1000000,
			(long)anotherCoupleMonthExpenseAvg, coupleMonthExpense);

	}

	public List<CoupleExpenseStatisticsDto> getCoupleExpenseStatistics(long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		Long totalAmount = expenseRepository.findTotalExpenditureForMonth(couple.getCoupleId(),
			LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getYear());

		if (totalAmount == 0) {
			return null;
		}

		return expenseRepository.findCategoryWiseExpenditureForMonth(couple.getCoupleId(),
				totalAmount).stream()
			.map(tuple -> new CoupleExpenseStatisticsDto(
				tuple.get(0, Category.class),
				tuple.get(1, Long.class),
				tuple.get(2, Double.class)
			))
			.collect(Collectors.toList());

	}
}
