package com.bbyuworld.gagyebbyu.domain.income.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeOverviewDto{
    private Long coupleId;
    private LocalDate date;
    private Long totalAmount;
}
