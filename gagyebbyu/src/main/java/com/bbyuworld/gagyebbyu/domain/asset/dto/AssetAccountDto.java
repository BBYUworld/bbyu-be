package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetAccountDto extends AssetDto {
    private String accountNumber;
    private String accountType;  // ENUM('SAVINGS', 'DEPOSIT', 'FREE_SAVINGS')
    private Long oneTimeTransferLimit;
    private Long dailyTransferLimit;
    private LocalDate maturityDate;
    private BigDecimal interestRate;
    private Integer term;
}