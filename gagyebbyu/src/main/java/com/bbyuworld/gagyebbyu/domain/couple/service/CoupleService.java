package com.bbyuworld.gagyebbyu.domain.couple.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.couple.dto.request.CoupleConnectDto;
import com.bbyuworld.gagyebbyu.domain.couple.dto.request.CoupleCreateDto;
import com.bbyuworld.gagyebbyu.domain.couple.dto.request.CoupleUpdateDto;
import com.bbyuworld.gagyebbyu.domain.couple.dto.response.CoupleResponseDto;
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

		Couple couple = coupleCreateDto.toEntity(user1);

		coupleRepository.save(couple);
	}

	@Transactional
	public void connectCouple(long coupleId, CoupleConnectDto coupleConnectDto) {
		User user2 = userRepository.findById(coupleConnectDto.getUser2Id())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(coupleId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		couple.updateStatusConnect(user2);

		couple.getUser1().updateCoupleId(couple.getCoupleId());
		user2.updateCoupleId(couple.getCoupleId());

		expenseRepository.updateExpenseCouple(couple, couple.getUser1().getUserId(), user2.getUserId());

		//자산도 추후 추가
	}

	@Transactional
	public void updateCouple(long userId, CoupleUpdateDto coupleUpdateDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		couple.updateCouple(
			coupleUpdateDto.getMarriedAt() != null ? coupleUpdateDto.getMarriedAt() : couple.getMarriedAt(),
			coupleUpdateDto.getNickname() != null ? coupleUpdateDto.getNickname() : couple.getNickname(),
			coupleUpdateDto.getMonthlyTargetAmount() != null ? coupleUpdateDto.getMonthlyTargetAmount() :
				couple.getMonthlyTargetAmount()
		);
	}

	public CoupleResponseDto getCouple(long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		return CoupleResponseDto.from(couple, ChronoUnit.DAYS.between(couple.getMarriedAt(), LocalDate.now()) + 1);
	}

}
