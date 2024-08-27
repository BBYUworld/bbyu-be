package com.bbyuworld.gagyebbyu.domain.fund.dto.response;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.fund.entity.FundTransaction;
import com.bbyuworld.gagyebbyu.domain.fund.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class FundTransactionDto {
	private long fundTransactionId;
	private String name;
	private long amount;
	private TransactionType type;
	private LocalDateTime date;
	private long currentAmount;

	public static FundTransactionDto from(FundTransaction fundTransaction) {
		return new FundTransactionDto(
			fundTransaction.getFundTransactionId(),
			fundTransaction.getUser().getName(),
			fundTransaction.getAmount(),
			fundTransaction.getType(),
			fundTransaction.getDate(),
			fundTransaction.getCurrentAmount()
		);
	}
}

