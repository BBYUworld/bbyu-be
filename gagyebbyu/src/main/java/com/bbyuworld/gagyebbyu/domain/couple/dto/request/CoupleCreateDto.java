package com.bbyuworld.gagyebbyu.domain.couple.dto.request;

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
	private long user2Id;

	public Couple toEntity(User user1, User user2) {
		return Couple.builder()
			.user1(user1)
			.user2(user2)
			.marriedAt(marriedAt)
			.monthlyTargetAmount(monthlyTargetAmount)
			.nickname(nickname)
			.build();
	}
}
