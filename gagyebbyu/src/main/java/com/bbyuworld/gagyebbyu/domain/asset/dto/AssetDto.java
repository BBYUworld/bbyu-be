package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetDto {
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
}
