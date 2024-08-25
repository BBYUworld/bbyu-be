package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetLoanDto extends AssetDto {
    private String loanName;
    private BigDecimal interestRate;
    private Long remainedAmount;
}
