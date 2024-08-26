package com.bbyuworld.gagyebbyu.domain.couple.entity;

import java.time.LocalDate;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "couple")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coupleId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user1_id", referencedColumnName = "userId", nullable = false)
	private User user1;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user2_id", referencedColumnName = "userId")
	private User user2;

	@Column(length = 10)
	private String nickname;

	@Column(name = "married_at")
	private LocalDate marriedAt;

	@Column(name = "monthly_target_amount")
	private Long monthlyTargetAmount;

	@Column
	@Enumerated(EnumType.STRING)
	private Status status;

	@Builder
	public Couple(User user1, String nickname, LocalDate marriedAt, Long monthlyTargetAmount) {
		this.user1 = user1;
		this.nickname = nickname;
		this.marriedAt = marriedAt;
		this.monthlyTargetAmount = monthlyTargetAmount;
		this.status = Status.WAIT;
	}

	public void updateTargetAmount(Long monthlyTargetAmount) {
		this.monthlyTargetAmount = monthlyTargetAmount;
	}

	public void updateCouple(LocalDate marriedAt, String nickname, Long monthlyTargetAmount) {
		this.marriedAt = marriedAt;
		this.nickname = nickname;
		this.monthlyTargetAmount = monthlyTargetAmount;
	}

	public void updateStatusConnect(User user2) {
		this.user2 = user2;
		this.status = Status.CONNECT;
	}
}
