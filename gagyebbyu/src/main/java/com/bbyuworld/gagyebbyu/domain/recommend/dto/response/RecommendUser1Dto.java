package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import com.bbyuworld.gagyebbyu.domain.loan.entity.Loan;
import com.bbyuworld.gagyebbyu.domain.loan.entity.RatingName;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendUser1Dto {
	private long loanId;
	private double interestRate;
	private int loanLimit;
	private String bankName;
	private String loanName;
	private RatingName ratingName;
	private int loanTermMonths;
	private int creditScoreRequirement;
	private double ratio;
	private double dsr;
	private double stressDsr;

	public static RecommendUser1Dto from(Loan loan, RecommendCompareDto dto) {
		return new RecommendUser1Dto(
			loan.getLoanId(),
			dto.getMale_interest_rate(),
			dto.getMale_loan_limit(),
			loan.getBankName(),
			loan.getLoanName(),
			loan.getRatingName(),
			dto.getMale_loan_term_months(),
			dto.getMale_credit_score_requirement(),
			dto.getMale_ratio(),
			dto.getMale_dsr(),
			dto.getMale_stress_dsr()
		);
	}
}

