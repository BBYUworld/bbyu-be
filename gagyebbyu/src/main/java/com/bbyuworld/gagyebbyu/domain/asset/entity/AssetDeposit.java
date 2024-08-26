package com.bbyuworld.gagyebbyu.domain.asset.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset_Deposit")
@Getter
public class AssetDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetDepositId;

    @OneToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private String bank;

    @Column(name = "bank_code")
    private String bankCode;

    private String number;

    private String type;

    private Long amount;

    private Boolean hidden;

    @Column(name = "daily_transfer_limit")
    private Long dailyTransferLimit;
    @Column(name = "one_time_transfer_limit")
    private Long oneTimeTransferLimit;
}
