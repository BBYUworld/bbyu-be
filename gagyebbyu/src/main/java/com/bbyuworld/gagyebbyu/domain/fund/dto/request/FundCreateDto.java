package com.bbyuworld.gagyebbyu.domain.fund.dto.request;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class FundCreateDto {

	private String goal;
	private long targetAmount;

	public Fund toEntity(Couple couple) {
		return Fund.builder()
			.couple(couple)
			.goal(goal)
			.targetAmount(targetAmount)
			.build();
	}
}