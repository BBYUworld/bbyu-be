package com.bbyuworld.gagyebbyu.domain.fund.service;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundOverViewDto;
import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;
import com.bbyuworld.gagyebbyu.domain.fund.repository.FundRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundService {
	private final FundRepository fundRepository;

	public FundOverViewDto getFund(long coupleId) {
		Fund fund = fundRepository.findByCoupleId(coupleId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.FUND_NOT_FOUND));

		return FundOverViewDto.from(fund);

	}
}
