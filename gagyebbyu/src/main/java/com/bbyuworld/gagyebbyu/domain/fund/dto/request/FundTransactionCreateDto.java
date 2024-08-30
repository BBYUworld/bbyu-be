package com.bbyuworld.gagyebbyu.domain.fund.dto.request;

import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;
import com.bbyuworld.gagyebbyu.domain.fund.entity.FundTransaction;
import com.bbyuworld.gagyebbyu.domain.fund.entity.TransactionType;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FundTransactionCreateDto {

	private long amount;
	private TransactionType type;
	private String accountNo;

	public FundTransaction toEntity(User user, Fund fund) {
		return FundTransaction.builder()
			.user(user)
			.couple(fund.getCouple())
			.amount(amount)
			.type(type)
			.fund(fund)
			.build();
	}
}
