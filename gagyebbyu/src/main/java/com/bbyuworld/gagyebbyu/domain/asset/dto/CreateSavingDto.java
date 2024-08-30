package com.bbyuworld.gagyebbyu.domain.asset.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSavingDto {
    private String bankCode;
    private String bankName;
    private String accountNo;
    private String withdrawalBankCode;
    private String withdrawalAccountNo;
    private String accountName;
    private String interestRate;
    private String subscriptionPeriod;
    private Long depositBalance;
    private String accountCreateDate;
    private String accountExpiryDate;
}
