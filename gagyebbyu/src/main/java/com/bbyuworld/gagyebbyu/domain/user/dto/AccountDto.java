package com.bbyuworld.gagyebbyu.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private String bankCode;
    private String bankName;
    private String userName;
    private String accountNo;
    private String accountName;
    private String accountTypeCode;
    private String accountCreatedDate;
    private String accountExpiryDate;
    private Long dailyTransferLimit;
    private Long oneTimeTransferLimit;
    private Long accountBalance;
    private String lastTransferDate;
    private String currency;
}
