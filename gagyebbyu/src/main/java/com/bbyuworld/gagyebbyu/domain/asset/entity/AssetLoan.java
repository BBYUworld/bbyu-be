package com.bbyuworld.gagyebbyu.domain.asset.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "asset_loan")
@PrimaryKeyJoinColumn(name = "asset_id")
@DiscriminatorValue("LOAN")
@Getter
@Setter
public class AssetLoan extends Asset {

    @Column(name = "loan_name", nullable = false)
    private String loanName;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "remained_amount", nullable = false)
    private Long remainedAmount;
}