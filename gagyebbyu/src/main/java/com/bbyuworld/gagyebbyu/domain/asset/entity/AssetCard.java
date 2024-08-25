package com.bbyuworld.gagyebbyu.domain.asset.entity;

import com.bbyuworld.gagyebbyu.domain.asset.enums.CardType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "asset_card")
@DiscriminatorValue("CARD")
@Getter
@Setter
public class AssetCard extends Asset {
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;
}


