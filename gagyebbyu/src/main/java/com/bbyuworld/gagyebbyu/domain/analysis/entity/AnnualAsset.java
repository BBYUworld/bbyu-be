package com.bbyuworld.gagyebbyu.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

@Entity
@Table(name = "annual_asset")
@Getter
@NoArgsConstructor
public class AnnualAsset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "couple_id", nullable = false)
	private Couple couple;

	@Column(name = "year", nullable = false)
	private int year;

	@Column(name = "cash_assets", nullable = false)
	private Long cashAssets;

	@Column(name = "investment_assets", nullable = false)
	private Long investmentAssets;

	@Column(name = "real_estate_assets", nullable = false)
	private Long realEstateAssets;

	@Column(name = "loan_assets", nullable = false)
	private Long loanAssets;

	@Column(name = "total_assets", nullable = false)
	private Long totalAssets;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

}
