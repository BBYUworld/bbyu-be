package com.bbyuworld.gagyebbyu.domain.loan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.loan.entity.Loan;
import com.bbyuworld.gagyebbyu.domain.loan.entity.RatingName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponseDto {
	private Long loanId;
	private String bankCode;
	private String bankName;
	private RatingName ratingName;
	private String loanName;
	private Integer loanPeriod;
	private Long minBalance;
	private Long maxBalance;
	private BigDecimal interestRate;
	private String accountType;
	private String loanTypeCode;
	private String loanTypeName;
	private String repaymentCode;
	private String repaymentName;
	private LocalDateTime startDate;

	public static LoanResponseDto from(Loan loan) {
		return new LoanResponseDto(
			loan.getLoanId(),
			loan.getBankCode(),
			loan.getBankName(),
			loan.getRatingName(),
			loan.getLoanName(),
			loan.getLoanPeriod(),
			loan.getMinBalance(),
			loan.getMaxBalance(),
			loan.getInterestRate(),
			loan.getAccountType(),
			loan.getLoanTypeCode(),
			loan.getLoanTypeName(),
			loan.getRepaymentCode(),
			loan.getRepaymentName(),
			loan.getStartDate()
		);
	}

}
