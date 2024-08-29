
package com.bbyuworld.gagyebbyu.domain.analysis.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnualAssetDto {
	private final int year;
	private final Long accountAssets;    // 변경된 부분
	private final Long stockAssets;      // 변경된 부분
	private final Long realEstateAssets;
	private final Long loanAssets;
	private final Long totalAssets;

	public AnnualAssetDto(int year, Long accountAssets, Long stockAssets, Long realEstateAssets, Long loanAssets, Long totalAssets) {
		this.year = year;
		this.accountAssets = accountAssets;      // 변경된 부분
		this.stockAssets = stockAssets;          // 변경된 부분
		this.realEstateAssets = realEstateAssets;
		this.loanAssets = loanAssets;
		this.totalAssets = totalAssets;
	}
}
