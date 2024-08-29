package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import lombok.Data;

@Data
public class RecommendCompareDto {
	private long totalPayment;
	private RecommendUser1Dto recommendUser1;
	private RecommendUser2Dto recommendUser2;
}