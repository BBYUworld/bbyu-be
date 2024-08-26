package com.bbyuworld.gagyebbyu.domain.fund.dto.response;

import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class FundStatusDto {
	private boolean isAchieved;
	private float percentage;
	private long currentAmount;

	public static FundStatusDto from(Fund fund) {

		float percentage = Float.parseFloat(
			String.format("%.2f", (double)fund.getCurrentAmount() / fund.getTargetAmount() * 100));

		System.out.println(fund.getCurrentAmount());
		return new FundStatusDto(
			fund.isEnded(),
			percentage,
			fund.getCurrentAmount()
		);
	}
}