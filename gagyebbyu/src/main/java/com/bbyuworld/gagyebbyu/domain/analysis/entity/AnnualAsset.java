package com.bbyuworld.gagyebbyu.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;

@Entity
@Table(name = "annual_asset", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"couple_id", "year"})
})
@Getter
@NoArgsConstructor
public class AnnualAsset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "couple_id", nullable = false)
	private Couple couple;

	@Column(nullable = false)
	private int year;

	@Column(name = "cash_assets", nullable = false)
	private Long cashAssets = 0L;

	@Column(name = "investment_assets", nullable = false)
	private Long investmentAssets = 0L;

	@Column(name = "real_estate_assets", nullable = false)
	private Long realEstateAssets = 0L;

	@Column(name = "total_assets", nullable = false)
	private Long totalAssets = 0L;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	@Builder
	public AnnualAsset(Couple couple, int year, Long cashAssets, Long investmentAssets, Long realEstateAssets, Long totalAssets) {
		this.couple = couple;
		this.year = year;
		this.cashAssets = cashAssets != null ? cashAssets : 0L;
		this.investmentAssets = investmentAssets != null ? investmentAssets : 0L;
		this.realEstateAssets = realEstateAssets != null ? realEstateAssets : 0L;
		this.totalAssets = totalAssets != null ? totalAssets : 0L;
	}
}
