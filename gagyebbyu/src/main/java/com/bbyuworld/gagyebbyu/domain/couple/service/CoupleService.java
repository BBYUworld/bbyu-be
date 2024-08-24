package com.bbyuworld.gagyebbyu.domain.couple.service;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.couple.dto.request.CoupleCreateDto;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.repository.ExpenseRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoupleService {

	private final CoupleRepository coupleRepository;
	private final UserRepository userRepository;
	private final ExpenseRepository expenseRepository;

	@Transactional
	public void createCouple(CoupleCreateDto coupleCreateDto) {
		User user1 = userRepository.findById(coupleCreateDto.getUser1Id())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		User user2 = userRepository.findById(coupleCreateDto.getUser2Id())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleCreateDto.toEntity(user1, user2);

		coupleRepository.save(couple);

		user1.updateCoupleId(couple.getCoupleId());
		user2.updateCoupleId(couple.getCoupleId());

		expenseRepository.updateExpenseCouple(couple, user1.getUserId(), user2.getUserId());

		//자산도 추후 추가

	}

}
