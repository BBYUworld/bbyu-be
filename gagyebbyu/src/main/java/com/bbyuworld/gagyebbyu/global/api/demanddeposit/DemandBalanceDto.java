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
public class DemandBalanceDto {
	private String bankCode;
	private String accountNo;
	private String accountBalance;
	private String accountCreatedDate;
	private String accountExpiryDate;
	private String lastTransactionDate;
	private String currency;
}
