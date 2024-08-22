package com.bbyuworld.gagyebbyu.domain.couple.entity;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@JoinColumn(name = "user1_id", referencedColumnName = "userId")
	private User user1;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user2_id", referencedColumnName = "userId")
	private User user2;

	@Column(length = 10)
	private String nickname; // 커플 닉네임

	@Column(name = "married_at")
	private LocalDateTime marriedAt; // 커플 기념일(결혼기념일)

	@Column(name = "monthly_target_amount")
	private Long monthlyTargetAmount; // 목표 한 달 지출 금액

	@Builder
	public Couple(User user1, User user2, String nickname, LocalDateTime marriedAt, Long monthlyTargetAmount) {
		this.user1 = user1;
		this.user2 = user2;
		this.nickname = nickname;
		this.marriedAt = marriedAt;
		this.monthlyTargetAmount = monthlyTargetAmount;
	}
}
