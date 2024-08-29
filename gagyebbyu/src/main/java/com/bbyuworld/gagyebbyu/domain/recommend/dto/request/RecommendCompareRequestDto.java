package com.bbyuworld.gagyebbyu.domain.recommend.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class RecommendCompareRequestDto {
	private long male_income;
	private long female_income;
	private long male_debt;
	private long female_debt;
	private long target_amount;
	private double stress_rate;
	private long male_credit_score;
	private long female_credit_score;
}
