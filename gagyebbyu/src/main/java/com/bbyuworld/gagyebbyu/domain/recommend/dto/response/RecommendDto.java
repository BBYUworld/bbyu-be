package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendDto {
	private double totalPayment;
	private RecommendUser1Dto recommendUser1;
	private RecommendUser2Dto recommendUser2;
}
