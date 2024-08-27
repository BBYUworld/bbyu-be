package com.bbyuworld.gagyebbyu.domain.asset.dto;

import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetAccountDto extends AssetDto {

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

    private String accountNumber;
    private String accountType;  // ENUM('SAVINGS', 'DEPOSIT', 'FREE_SAVINGS')
    private Long oneTimeTransferLimit;
    private Long dailyTransferLimit;
    private LocalDate maturityDate;
    private BigDecimal interestRate;
    private Integer term;
}