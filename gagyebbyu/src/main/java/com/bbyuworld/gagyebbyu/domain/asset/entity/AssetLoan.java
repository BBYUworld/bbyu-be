package com.bbyuworld.gagyebbyu.domain.asset.entity;

import com.bbyuworld.gagyebbyu.domain.asset.enums.LoanType;
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

    @Column(name = "initial_amount", nullable = false)
    private Long initialAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false)
    private LoanType loanType;

    @PreUpdate
    public void preUpdate() {
        if (this.initialAmount <= 0) {
            this.setIsEnded(true);
            this.setIsHidden(true);
        }
    }
}