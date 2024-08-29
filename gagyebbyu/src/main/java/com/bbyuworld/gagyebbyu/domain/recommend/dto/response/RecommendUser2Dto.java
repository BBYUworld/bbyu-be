package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import com.bbyuworld.gagyebbyu.domain.loan.entity.Loan;
import com.bbyuworld.gagyebbyu.domain.loan.entity.RatingName;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecommendUser2Dto {
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

	public static RecommendUser2Dto from(Loan loan, RecommendCompareDto dto) {
		return new RecommendUser2Dto(
			loan.getLoanId(),
			dto.getFemale_interest_rate(),
			dto.getFemale_loan_limit(),
			loan.getBankName(),
			loan.getLoanName(),
			loan.getRatingName(),
			dto.getFemale_loan_term_months(),
			dto.getFemale_credit_score_requirement(),
			dto.getFemale_ratio(),
			dto.getFemale_dsr(),
			dto.getFemale_stress_dsr()
		);
	}
}
