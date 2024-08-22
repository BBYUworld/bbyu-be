package com.bbyuworld.gagyebbyu.domain.fund.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class FundCreateDto {

	private String goal;
	private long targetAmount;
	private long currentAmount;

	// public Fund toEntity(Couple couple) {
	// 	return Fund.builder()
	// 		.couple(couple)
	// 		.goal(goal)
	// 		.targetAmount(targetAmount)
	// 		.build();
	// }
}