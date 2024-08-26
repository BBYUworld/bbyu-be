package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetCardDto extends AssetDto {

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

    private String cardNumber;
    private String cardName;
    private String cardType;  // ENUM('CREDIT', 'CHECK')


}
