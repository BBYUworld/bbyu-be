package com.bbyuworld.gagyebbyu.domain.income.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeMonthDto {
    private Long totalAmount;
    private Long targetAmount;
    private Long amountDifference;
    private List<IncomeOverviewDto> incomes;
}
