package com.bbyuworld.gagyebbyu.domain.fund.dto.response;

import java.time.LocalDateTime;

import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class FundOverViewDto {
	private long fundId;
	private String goal;
	private long targetAmount;
	private long currentAmount;
	private long isEmergencyUsed;
	private int emergencyCount;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public static FundOverViewDto from(Fund fund) {
		return new FundOverViewDto(
			fund.getFundId(),
			fund.getGoal(),
			fund.getTargetAmount(),
			fund.getCurrentAmount(),
			fund.getEmergency(),
			fund.getEmergency(),
			fund.getStartDate(),
			fund.getEndDate()
		);
	}
}
