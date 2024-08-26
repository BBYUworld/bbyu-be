package com.bbyuworld.gagyebbyu.domain.expense.dto.param;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ExpenseParam {

	/**
	 * default 현재 일
	 * month : 1, 2, 3, 4, 5, 6, 7, 8, ...
	 */
	private Integer day;

	/**
	 * default 현재 달
	 * month : 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
	 */
	private Integer month;

	/**
	 * default : 현재 년도
	 * year : 2024, 2023, 2021, ...
	 */
	private Integer year;

	/**
	 * default : asc
	 * sort : asc, desc
	 */
	private String sort;

	@Override
	public String toString() {
		return "ExpenseParam{" +
				"day=" + day +
				", month=" + month +
				", year=" + year +
				", sort='" + sort + '\'' +
				"}\n";
	}
}
