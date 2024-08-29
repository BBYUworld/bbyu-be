package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import com.bbyuworld.gagyebbyu.domain.loan.dto.response.LoanResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public final class RecommendLoanDto {
	private Long loan_id;
	private double pred;
	private LoanResponseDto loanDto;
}
