package com.bbyuworld.gagyebbyu.domain.analysis.service;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.analysis.dto.response.CoupleExpenseResultDto;
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

	public CoupleExpenseResultDto getCoupleExpenseResult(long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		log.info("{}", couple.getUser1().getAge());
		log.info("{}", couple.getUser1().getMonthlyIncome());

		log.info("{}", couple.getUser2().getAge());
		log.info("{}", couple.getUser2().getMonthlyIncome());

		int avgAge = (couple.getUser1().getAge() + couple.getUser2().getAge()) / 2;

		int startAge = avgAge / 10 * 10;
		int endAge = startAge + 9;

		log.info("평균 Age {}", avgAge);

		long avgIncome = (couple.getUser1().getMonthlyIncome() + couple.getUser2().getMonthlyIncome()) / 2;

		long startIncome = avgIncome / 100000 * 100000 - 1000000;
		long endIncome = startIncome + 1000000;
		log.info("평균 income {}", avgIncome);

		System.out.println(startAge + " " + endAge + " " + startIncome + " " + endIncome);

		double anotherCoupleMonthExpenseAvg = expenseRepository.findAverageExpenditureForEligibleCouples(
			startAge, endAge, startIncome, endIncome);

		System.out.println(anotherCoupleMonthExpenseAvg);

		Category category = expenseRepository.findTopCategoryForCoupleLastMonth(couple.getCoupleId());

		System.out.println(category);

		long coupleMonthExpense = expenseRepository.findTotalExpenditureForCoupleLastMonth(couple.getCoupleId());

		return new CoupleExpenseResultDto(category, startAge, startIncome + 1000000,
			(long)anotherCoupleMonthExpenseAvg, coupleMonthExpense);

	}
}
