package com.bbyuworld.gagyebbyu.domain.fund.entity;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "fund")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fund {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fundId;

	@ManyToOne
	@JoinColumn(name = "couple_id", nullable = false)
	private Couple couple;

	@Column(name = "goal", nullable = false)
	private String goal;

	@Column(name = "target_amount", nullable = false)
	private long targetAmount;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@Column(name = "current_amount", nullable = false)
	private long currentAmount;

	@Column(name = "emergency", nullable = false)
	private int emergency;

	@Column(name = "is_ended", nullable = false)
	private boolean isEnded;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	@Builder
	public Fund(Couple couple, String goal, long targetAmount) {
		this.couple = couple;
		this.goal = goal;
		this.targetAmount = targetAmount;
		this.startDate = LocalDateTime.now();
		this.endDate = LocalDateTime.now();
		this.currentAmount = 0;
		this.emergency = 0;
		this.isEnded = false;
		this.isDeleted = false;
	}

	public void updateFund(long money, TransactionType type) {
		if (type == TransactionType.MINUS) {
			currentAmount -= money;
			emergency += 1;
		}
		if (type == TransactionType.PLUS) {
			currentAmount += money;
		}
	}

	public void updateStatus() {
		this.isEnded = true;
	}

	public void deleteFund() {
		this.isDeleted = true;
	}

}
