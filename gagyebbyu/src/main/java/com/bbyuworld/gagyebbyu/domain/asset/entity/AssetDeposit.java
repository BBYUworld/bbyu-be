package com.bbyuworld.gagyebbyu.domain.asset.entity;

import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "asset_deposit")
public class AssetDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetDepositId;

    @Column(name = "asset_id")
    private Long assetId;

    private String bank;
    private String number;
    private String type;
    private Long amount;

    @Column(nullable = false)
    private boolean hidden = false;


}