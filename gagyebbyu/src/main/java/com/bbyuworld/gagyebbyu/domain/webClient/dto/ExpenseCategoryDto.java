package com.bbyuworld.gagyebbyu.domain.webClient.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpenseCategoryDto {
	private String place;

	public ExpenseCategoryDto(String place) {
		this.place = place;
	}

}
