package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetCardDto extends AssetDto {
    private String cardNumber;
    private String cardName;
    private String cardType;  // ENUM('CREDIT', 'CHECK')
}
