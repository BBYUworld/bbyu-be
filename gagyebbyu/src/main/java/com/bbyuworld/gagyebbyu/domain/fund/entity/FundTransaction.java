package com.bbyuworld.gagyebbyu.domain.fund.entity;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fund_transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fundTransactionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "couple_id", referencedColumnName = "coupleId")
	private Couple couple;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fund_id", referencedColumnName = "fundId")
	private Fund fund;

	@Column(nullable = false)
	private Long amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;

	@Column(name = "date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime date;

	@Builder
	public FundTransaction(Couple couple, User user, Fund fund, Long amount, TransactionType type, LocalDateTime date) {
		this.couple = couple;
		this.user = user;
		this.fund = fund;
		this.amount = amount;
		this.type = type;
		this.date = LocalDateTime.now();
	}
}
