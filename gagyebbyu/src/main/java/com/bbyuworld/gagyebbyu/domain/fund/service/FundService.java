package com.bbyuworld.gagyebbyu.domain.fund.service;

import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.fund.dto.request.FundCreateDto;
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
	private final CoupleRepository coupleRepository;

	public FundOverViewDto getFund(long coupleId) {
		Fund fund = fundRepository.findByCouple_CoupleId(coupleId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.FUND_NOT_EXIST));

		return FundOverViewDto.from(fund);
	}

	public void createFund(long coupleId, FundCreateDto fundCreateDto) {
		Couple couple = coupleRepository.findById(coupleId)
			.orElseThrow(() -> new DataNotFoundException((ErrorCode.COUPLE_NOT_FOUND)));
		
		fundRepository.save(fundCreateDto.toEntity(couple));
	}
}
