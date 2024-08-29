package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import java.util.List;

import com.bbyuworld.gagyebbyu.domain.user.entity.Gender;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecommendResponseDto {
	private long user1Id;
	private long user2Id;
	private String user1Name;
	private String user2Name;
	private Gender user1Gender;
	private Gender user2Gender;
	private List<RecommendDto> coupleLoanRecommends;

	public static RecommendResponseDto from(User user1, User user2, List<RecommendDto> coupleLoanRecommends) {
		return new RecommendResponseDto(
			user1.getUserId(),
			user2.getUserId(),
			user1.getName(),
			user2.getName(),
			user1.getGender(),
			user2.getGender(),
			coupleLoanRecommends
		);
	}
}
