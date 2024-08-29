package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecommendResponseDto {
	private long user1Id;
	private long user2Id;
	private String user1Name;
	private String user2Name;
	private List<RecommendCompareDto> coupleLoanRecommends;
}
