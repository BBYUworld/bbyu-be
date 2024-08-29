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
@Table(name = "deposit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deposit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long depositId;

	@Column(name = "deposit_interest_rate", nullable = false)
	private double depositInterestRate;

	@Column(name = "term_months", nullable = false)
	private int termMonths;

	@Column(name = "min_deposit_amount", nullable = false)
	private long minDepositAmount;

	@Column(name = "max_deposit_amount", nullable = false)
	private long maxDepositAmount;

	@Column(name = "deposit_interest_payment_method", nullable = false)
	private String depositInterestPaymentMethod;

	@Column(name = "deposit_name", nullable = false)
	private String depositName;

	@Column(name = "bank_name", nullable = false)
	private String bankName;

	@Column(name = "description", nullable = false, length = 255)
	private String description;

	@Builder
	public Deposit(double depositInterestRate, int termMonths, long minDepositAmount,
		long maxDepositAmount, String depositInterestPaymentMethod, String depositName,
		String bankName, String description) {
		this.depositInterestRate = depositInterestRate;
		this.termMonths = termMonths;
		this.minDepositAmount = minDepositAmount;
		this.maxDepositAmount = maxDepositAmount;
		this.depositInterestPaymentMethod = depositInterestPaymentMethod;
		this.depositName = depositName;
		this.bankName = bankName;
		this.description = description;
	}
}
