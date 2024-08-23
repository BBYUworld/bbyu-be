package com.bbyuworld.gagyebbyu.domain.fund.service;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundConditionService {
	public void isAchievedFund(Fund fund) {
		if (fund.getCurrentAmount() >= fund.getTargetAmount()) {
			fund.updateStatus();
		}
	}

	public void isExceededEmergency(int emergency) {
		if (emergency >= 2) {
			throw new BadRequestException(ErrorCode.FUND_EXCEED_EMERGENCY);
		}
	}
}
