package com.bbyuworld.gagyebbyu.domain.analysis.dto.param;

import com.bbyuworld.gagyebbyu.domain.expense.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class AnalysisParam {

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
	 * category :
	 *     '교육',
	 *     '교통_자동차',
	 *     '기타소비',
	 *     '대형마트',
	 *     '미용',
	 *     '배달',
	 *     '보험',
	 *     '생필품',
	 *     '생활서비스',
	 *     '세금_공과금',
	 *     '쇼핑몰',
	 *     '여행_숙박',
	 *     '외식',
	 *     '의료_건강',
	 *     '주류_펍',
	 *     '취미_여가',
	 *     '카페',
	 *     '통신',
	 *     '편의점'
	 */
	private Category category;
}
