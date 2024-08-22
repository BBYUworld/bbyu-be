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

	@Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime endDate;

	@Column(name = "current_amount", nullable = false)
	private Long currentAmount;

	@Column(name = "emergency", nullable = false, columnDefinition = "INT DEFAULT 0")
	private int emergency;

	@Builder
	public Fund(Couple couple, String goal, long targetAmount, int emergency) {
		this.couple = couple;
		this.goal = goal;
		this.targetAmount = targetAmount;
		this.startDate = LocalDateTime.now();
		this.endDate = LocalDateTime.now();
		this.currentAmount = 0L;
		this.emergency = 0;
	}

	public void updateFund(long money) {
		currentAmount += money;
	}

}
