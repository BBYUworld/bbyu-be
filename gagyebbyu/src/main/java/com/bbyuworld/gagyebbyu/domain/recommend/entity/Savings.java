package com.bbyuworld.gagyebbyu.domain.recommend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "savings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Savings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long savingsId;

	@Column(name = "savings_interest_rate", nullable = false)
	private double savingsInterestRate;

	@Column(name = "term_months", nullable = false)
	private int termMonths;

	@Column(name = "min_savings_amount", nullable = false)
	private long minSavingsAmount;

	@Column(name = "max_savings_amount", nullable = false)
	private long maxSavingsAmount;

	@Column(name = "savings_interest_payment_method", nullable = false)
	private String savingsInterestPaymentMethod;

	@Column(name = "savings_name", nullable = false)
	private String savingsName;

	@Column(name = "bank_name", nullable = false)
	private String bankName;

	@Column(name = "description", nullable = false, length = 255)
	private String description;

	@Builder
	public Savings(double savingsInterestRate, int termMonths, long minSavingsAmount,
		long maxSavingsAmount, String savingsInterestPaymentMethod, String savingsName,
		String bankName, String description) {
		this.savingsInterestRate = savingsInterestRate;
		this.termMonths = termMonths;
		this.minSavingsAmount = minSavingsAmount;
		this.maxSavingsAmount = maxSavingsAmount;
		this.savingsInterestPaymentMethod = savingsInterestPaymentMethod;
		this.savingsName = savingsName;
		this.bankName = bankName;
		this.description = description;
	}
}
