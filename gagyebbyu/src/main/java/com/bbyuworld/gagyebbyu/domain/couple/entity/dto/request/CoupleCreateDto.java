package com.bbyuworld.gagyebbyu.domain.couple.entity.dto.request;

import java.time.LocalDate;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class CoupleCreateDto {
	private String nickname;
	private LocalDate marriedAt;
	private long monthlyTargetAmount;
	private long user1Id;

	public Couple toEntity(User user1) {
		return Couple.builder()
			.user1(user1)
			.marriedAt(marriedAt)
			.monthlyTargetAmount(monthlyTargetAmount)
			.nickname(nickname)
			.build();
	}
}
