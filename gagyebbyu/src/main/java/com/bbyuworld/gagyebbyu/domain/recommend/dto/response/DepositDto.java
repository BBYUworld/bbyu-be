package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import com.bbyuworld.gagyebbyu.domain.recommend.entity.Deposit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class DepositDto {
	private long depositId;
	private double depositInterestRate;
	private int termMonths;
	private long minDepositAmount;
	private long maxDepositAmount;
	private String depositInterestPaymentMethod;
	private String depositName;
	private String bankName;
	private String description;
	private String accountTypeUniqueNo;

	public static DepositDto from(Deposit deposit) {
		return new DepositDto(
			deposit.getDepositId(),
			deposit.getDepositInterestRate(),
			deposit.getTermMonths(),
			deposit.getMinDepositAmount(),
			deposit.getMaxDepositAmount(),
			deposit.getDepositInterestPaymentMethod(),
			deposit.getDepositName(),
			deposit.getBankName(),
			deposit.getDescription(),
				deposit.getAccountTypeUniqueNo()
		);
	}
}
