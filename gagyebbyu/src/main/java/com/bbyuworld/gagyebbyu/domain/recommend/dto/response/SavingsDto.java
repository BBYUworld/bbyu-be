package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import com.bbyuworld.gagyebbyu.domain.recommend.entity.Savings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class SavingsDto {
	private long savingsId;
	private double savingsInterestRate;
	private int termMonths;
	private long minSavingsAmount;
	private long maxSavingsAmount;
	private String savingsInterestPaymentMethod;
	private String savingsName;
	private String bankName;
	private String description;

	public static SavingsDto from(Savings savings) {
		return new SavingsDto(
			savings.getSavingsId(),
			savings.getSavingsInterestRate(),
			savings.getTermMonths(),
			savings.getMinSavingsAmount(),
			savings.getMaxSavingsAmount(),
			savings.getSavingsInterestPaymentMethod(),
			savings.getSavingsName(),
			savings.getBankName(),
			savings.getDescription()
		);
	}

	@Override
	public String toString() {
		return "SavingsDto{" +
				"savingsId=" + savingsId +
				", savingsInterestRate=" + savingsInterestRate +
				", termMonths=" + termMonths +
				", minSavingsAmount=" + minSavingsAmount +
				", maxSavingsAmount=" + maxSavingsAmount +
				", savingsInterestPaymentMethod='" + savingsInterestPaymentMethod + '\'' +
				", savingsName='" + savingsName + '\'' +
				", bankName='" + bankName + '\'' +
				", description='" + description + '\'' +
				"}\n";
	}
}
