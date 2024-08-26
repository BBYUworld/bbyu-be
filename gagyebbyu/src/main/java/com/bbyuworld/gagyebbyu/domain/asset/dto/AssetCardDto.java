package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetCardDto extends AssetDto {
    private String cardNumber;
    private String cardName;
    private String cardType;  // ENUM('CREDIT', 'CHECK')


}
