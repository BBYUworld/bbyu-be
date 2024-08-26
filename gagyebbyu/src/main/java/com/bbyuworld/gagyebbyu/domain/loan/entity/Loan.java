package com.bbyuworld.gagyebbyu.domain.loan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private long loanId;

    @Column(name = "bank_code", length = 10, nullable = false)
    private String bankCode;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating_name", nullable = false)
    private RatingName ratingName;

    @Column(name = "loan_name", nullable = false)
    private String loanName;

    @Column(name = "loan_period")
    private Integer loanPeriod;

    @Column(name = "min_balance", nullable = false)
    private Long minBalance;

    @Column(name = "max_balance", nullable = false)
    private Long maxBalance;

    @Column(name = "interest_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Column(name = "account_type", length = 50, nullable = false)
    private String accountType;

    @Column(name = "loan_type_code", length = 10, nullable = false)
    private String loanTypeCode;

    @Column(name = "loan_type_name", nullable = false)
    private String loanTypeName;

    @Column(name = "repayment_code", length = 10, nullable = false)
    private String repaymentCode;

    @Column(name = "repayment_name", nullable = false)
    private String repaymentName;

    @Column(name = "start_date")
    private LocalDateTime startDate;
}