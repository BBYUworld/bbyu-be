package com.bbyuworld.gagyebbyu.domain.asset.entity;

import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import lombok.Data;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "asset_account")
@DiscriminatorValue("ACCOUNT")
@Getter
@Setter
public class AssetAccount extends Asset {
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "one_time_transfer_limit")
    private Long oneTimeTransferLimit;

    @Column(name = "daily_transfer_limit")
    private Long dailyTransferLimit;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "term")
    private Integer term;
}