package com.bbyuworld.gagyebbyu.domain.couple.entity.dto.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class CoupleUpdateDto {
	private String nickname;
	private LocalDate marriedAt;
	private Long monthlyTargetAmount;
}
