package com.bbyuworld.gagyebbyu.domain.couple.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class CoupleUpdateDto {
	private String nickname;
	private LocalDateTime marriedAt;
	private Long monthlyTargetAmount;
}
