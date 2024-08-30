package com.bbyuworld.gagyebbyu.global.api.demanddeposit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositDto {
	private String transactionUniqueNo;
	private String transactionDate;
}
