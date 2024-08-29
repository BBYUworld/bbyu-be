package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import lombok.Data;

@Data
public class RecommendUser1Dto {
	private long loanId;
	private double interestRate;
	private int loanLimit;
	private String bankName;
	private String loanName;
	private String ratingName;
	private int loanTermMonths;
	private int creditScoreRequirement;
	private double ratio;
	private double totalPayment;
	private double dsr;
	private double stressDsr;
}
