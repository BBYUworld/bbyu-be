package com.bbyuworld.gagyebbyu.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(name = "couple_id")
	private Long coupleId;

	@Column(length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@Column(nullable = false)
	private Integer age;

	@Column
	private Long monthlyIncome;

	@Column(name = "rating_name")
	private String ratingName; // 신용등급

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@Column(length = 15, nullable = false)
	private String phone;

	@Column(name = "is_login", nullable = false)
	private boolean isLogin = false;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(length = 100)
	private String nickname;

	@Column(name = "monthly_target_amount")
	private Integer monthlyTargetAmount;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "access_token")
	private String accessToken;

	@Builder
	public User(String name, Gender gender, Integer age, Long monthlyIncome, String ratingName, boolean isDeleted,
		String phone, boolean isLogin, String email, String nickname, Integer monthlyTargetAmount,
		String refreshToken, String accessToken, Long coupleId) {
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.monthlyIncome = monthlyIncome;
		this.ratingName = ratingName;
		this.isDeleted = isDeleted;
		this.phone = phone;
		this.isLogin = isLogin;
		this.email = email;
		this.nickname = nickname;
		this.monthlyTargetAmount = monthlyTargetAmount;
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
		this.coupleId = coupleId;
	}
}
