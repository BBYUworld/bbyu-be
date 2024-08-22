package com.bbyuworld.gagyebbyu.domain.fund.service;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundValidService {
	public void isAchievedFund(Fund fund) {
		if (fund.getCurrentAmount() >= fund.getTargetAmount()) {
			fund.updateStatus();
		}
	}
}
