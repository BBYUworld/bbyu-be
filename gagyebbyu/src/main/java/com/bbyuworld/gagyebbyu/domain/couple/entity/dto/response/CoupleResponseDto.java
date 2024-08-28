package com.bbyuworld.gagyebbyu.domain.couple.entity.dto.response;

import java.time.LocalDate;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoupleResponseDto {
	private Long coupleId;
	private String nickname;
	private LocalDate marriedAt;
	private Long monthlyTargetAmount;
	private String user1Name;
	private String user2Name;
	private Long marriedPeriod;

	public static CoupleResponseDto from(Couple couple, Long marriedPeriod) {
		return new CoupleResponseDto(
			couple.getCoupleId(),
			couple.getNickname(),
			couple.getMarriedAt(),
			couple.getMonthlyTargetAmount(),
			couple.getUser1().getName(),
			couple.getUser2().getName(),
			marriedPeriod
		);
	}
}
