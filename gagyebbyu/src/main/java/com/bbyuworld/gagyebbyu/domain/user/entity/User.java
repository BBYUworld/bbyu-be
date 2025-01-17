package com.bbyuworld.gagyebbyu.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.loan.entity.RatingName;
import com.bbyuworld.gagyebbyu.domain.user.dto.UserDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
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
	@Column(name = "gender")
	private Gender gender;

	@Column(name = "age")
	private Integer age;

	@Column(name = "address")
	private String address;

	@Column
	private Long monthlyIncome;

	@Column(name = "rating_name")
	private String ratingName;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@Column(length = 15, nullable = false)
	private String phone;

	@Column(name = "is_login", nullable = false)
	private boolean isLogin = false;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(length = 100)
	private String nickname;

	@Column(name = "monthly_target_amount")
	private Long monthlyTargetAmount;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "api_key")
	private String apiKey;

	@Column(name = "region")
	@Enumerated(EnumType.STRING)
	private Region region;

	@Column(name = "credit_score")
	private Integer creditScore;

	@Column
	@Enumerated(EnumType.STRING)
	private Occupation occupation;

	@Column(name = "late_payment")
	Boolean latePayment = false;

	@Column(name = "financial_accident")
	Integer financialAccident = 0;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Asset> assets = new ArrayList<>();

	@Builder
	public User(String name, Gender gender, Integer age, Long monthlyIncome, String ratingName, boolean isDeleted,
		String phone, boolean isLogin, String email, String nickname, Long monthlyTargetAmount,
		String refreshToken, String accessToken, Long coupleId, String password, String apiKey, String address,
		Occupation occupation, Region region, Boolean latePayment, Integer financialAccident) {
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.address = address;
		this.monthlyIncome = monthlyIncome;
		this.ratingName = ratingName;
		this.isDeleted = isDeleted;
		this.phone = phone;
		this.isLogin = isLogin;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.monthlyTargetAmount = monthlyTargetAmount;
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
		this.coupleId = coupleId;
		this.apiKey = apiKey;
		this.region = region;
		this.occupation = occupation;
		this.latePayment = latePayment;
		this.financialAccident = financialAccident;
	}

	public UserDto toDto() {
		return UserDto.builder()
			.userId(this.userId)
			.coupleId(this.coupleId)
			.name(this.name)
			.gender(this.gender)
			.age(this.age)
			.address(this.address)
			.monthlyIncome(this.monthlyIncome)
			.ratingName(this.ratingName)
			.isDeleted(this.isDeleted)
			.phone(this.phone)
			.isLogin(this.isLogin)
			.email(this.email)
			.password(this.password)
			.nickname(this.nickname)
			.monthlyTargetAmount(this.monthlyTargetAmount)
			.refreshToken(this.refreshToken)
			.accessToken(this.accessToken)
			.build();
	}

	// Converts UserDto to User entity
	public static User fromDto(UserDto userDto) {
		return User.builder()
			.coupleId(userDto.getCoupleId())
			.name(userDto.getName())
			.gender(userDto.getGender())
			.age(userDto.getAge())
			.address(userDto.getAddress())
			.monthlyIncome(userDto.getMonthlyIncome())
			.ratingName(userDto.getRatingName())
			.isDeleted(userDto.isDeleted())
			.phone(userDto.getPhone())
			.isLogin(userDto.isLogin())
			.email(userDto.getEmail())
			.password(userDto.getPassword())
			.nickname(userDto.getNickname())
			.monthlyTargetAmount(userDto.getMonthlyTargetAmount())
			.refreshToken(userDto.getRefreshToken())
			.accessToken(userDto.getAccessToken())
			.apiKey(userDto.getApiKey())
			.occupation(userDto.getOccupation())
			.financialAccident(userDto.getFinancialAccident())
			.region(userDto.getRegion())
			.latePayment(userDto.getLatePayment())
			.build();
	}

	// Updates User entity's properties from a UserDto
	public void setProperties(UserDto userDto) {
		//		if (userDto.getCoupleId != null) this.coupleId = userDto.getCoupleId();
		if (userDto.getName() != null)
			this.name = userDto.getName();
		if (userDto.getGender() != null)
			this.gender = userDto.getGender();
		if (userDto.getAge() != null)
			this.age = userDto.getAge();
		if (userDto.getMonthlyIncome() != null)
			this.monthlyIncome = userDto.getMonthlyIncome();
		if (userDto.getRatingName() != null)
			this.ratingName = userDto.getRatingName();
		this.isDeleted = userDto.isDeleted();
		if (userDto.getPhone() != null)
			this.phone = userDto.getPhone();
		if (userDto.getAddress() != null)
			this.address = userDto.getAddress();
		this.isLogin = userDto.isLogin();
		if (userDto.getEmail() != null)
			this.email = userDto.getEmail();
		if (userDto.getNickname() != null)
			this.nickname = userDto.getNickname();
		if (userDto.getMonthlyTargetAmount() != null)
			this.monthlyTargetAmount = userDto.getMonthlyTargetAmount();
		if (userDto.getRefreshToken() != null)
			this.refreshToken = userDto.getRefreshToken();
		if (userDto.getAccessToken() != null)
			this.accessToken = userDto.getAccessToken();
	}

	public void updateTargetAmount(Long monthlyTargetAmount) {
		this.monthlyTargetAmount = monthlyTargetAmount;
	}

	public void addAsset(Asset asset) {
		this.assets.add(asset);
		asset.setUser(this);
	}

	public void removeAsset(Asset asset) {
		this.assets.remove(asset);
		asset.setUser(null);
	}

	public void updateCoupleId(Long coupleId) {
		this.coupleId = coupleId;
	}
}
