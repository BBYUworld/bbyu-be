package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDepositDto {
    private String bankCode;
    private String bankName;
    private String accountNo;
    private String accountName;
    private String withdrawalBankCode;
    private String withdrawalAccountNo;
    private String subscriptionPeriod;
    private Long depositBalance;
    private String interestRate;
    private String accountCreateDate;
    private String accountExpiryDate;
}
