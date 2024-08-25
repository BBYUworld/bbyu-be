package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDepositDto {
    private Long assetDepositId;
    private Long assetId;
    private String bank;
    private String number;
    private String type;
    private Long amount;
    private boolean hidden;
}