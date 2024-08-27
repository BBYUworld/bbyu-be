package com.bbyuworld.gagyebbyu.domain.analysis.service;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.CoupleExpenseResultDto;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.repository.ExpenseRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoupleExpenseService {
	private final UserRepository userRepository;
	private final CoupleRepository coupleRepository;
	private final ExpenseRepository expenseRepository;

	public CoupleExpenseResultDto getCoupleExpenseResult(long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		int avgAge = (couple.getUser1().getAge() + couple.getUser2().getAge()) / 2;

		long avgIncome = (couple.getUser1().getMonthlyIncome() + couple.getUser2().getMonthlyIncome()) / 2;

		double anotherCoupleMonthExpenseAvg = expenseRepository.findAverageExpenditureForEligibleCouples(
			avgAge / 10 * 10, avgAge / 10 * 10 + 9, avgIncome / 100000 * 100000, avgIncome / 100000 * 100000 + 100000);

		String category = expenseRepository.findTopCategoryForCoupleLastMonth(couple.getCoupleId());

		long coupleMonthExpense = expenseRepository.findTotalExpenditureForCoupleLastMonth(couple.getCoupleId());

		return new CoupleExpenseResultDto(category, avgAge / 10 * 10, avgIncome / 100000 * 100000,
			(long)anotherCoupleMonthExpenseAvg, coupleMonthExpense);

	}
}
