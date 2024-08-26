package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetLoanDto extends AssetDto {

    private Long assetId;
    private Long userId;
    private Long coupleId;
    private String type;
    private String bankName;
    private String bankCode;
    private Long amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isEnded;
    private Boolean isHidden;

    private String loanName;
    private BigDecimal interestRate;
    private Long remainedAmount;
}
