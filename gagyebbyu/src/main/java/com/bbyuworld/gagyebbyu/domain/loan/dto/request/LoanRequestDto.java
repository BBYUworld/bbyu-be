package com.bbyuworld.gagyebbyu.domain.loan.dto.request;

import com.bbyuworld.gagyebbyu.domain.loan.entity.RatingName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDto {
    private String bankName;
    private RatingName ratingName;
    private String loanName;
    private Integer loanPeriod;
    private Long minBalance;
    private Long maxBalance;
    private BigDecimal interestRate;
    private String accountType;
    private String loanTypeCode;
    private String repaymentCode;
    private LocalDateTime startDate;
}