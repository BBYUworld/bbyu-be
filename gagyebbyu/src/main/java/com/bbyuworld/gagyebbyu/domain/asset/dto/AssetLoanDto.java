package com.bbyuworld.gagyebbyu.domain.asset.dto;

import com.bbyuworld.gagyebbyu.domain.asset.enums.LoanType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AssetLoanDto extends AssetDto {

    private Long assetId;
    private Long userId;
    private Long coupleId;
    private Long user1Id;
    private Long user2Id;
    private String user1Name;
    private String user2Name;
    private String user1ratingName;
    private String user2ratingName;
    private String type;
    private String bankName;
    private String bankCode;
    private Long amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isEnded;
    private Boolean isHidden;

    private String loanName;
    private BigDecimal interestRate;
    private Long initialAmount;
    private Long currentAmount;
    private LoanType loan_type_name;
}
