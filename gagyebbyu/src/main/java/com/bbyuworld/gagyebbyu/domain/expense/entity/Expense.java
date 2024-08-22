package com.bbyuworld.gagyebbyu.domain.expense.entity;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "expense")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long expenseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "couple_id", nullable = false)
	private Couple couple;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	private String category;

	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime date;

	@Column(columnDefinition = "TEXT")
	private String memo;

	@Column(length = 255)
	private String place;

	@Builder
	public Expense(User user, Couple couple, Long amount, String category, LocalDateTime date, String memo,
		String place) {
		this.user = user;
		this.couple = couple;
		this.amount = amount;
		this.category = category;
		this.date = date != null ? date : LocalDateTime.now();
		this.memo = memo;
		this.place = place;
	}
}
