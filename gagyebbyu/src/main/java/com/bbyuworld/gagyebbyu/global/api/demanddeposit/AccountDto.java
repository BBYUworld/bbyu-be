package com.bbyuworld.gagyebbyu.global.api.demanddeposit;

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
    private String accountTypeName;
    private String accountCreatedDate;
    private String accountExpiryDate;
    private Long dailyTransferLimit;
    private Long oneTimeTransferLimit;
    private Long accountBalance;
    private String lastTransactionDate;
    private String currency;

    @Override
    public String toString() {
        return "AccountDto{" +
                "bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", userName='" + userName + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountTypeCode='" + accountTypeCode + '\'' +
                ", accountTypeName='" + accountTypeName + '\'' +
                ", accountCreatedDate='" + accountCreatedDate + '\'' +
                ", accountExpiryDate='" + accountExpiryDate + '\'' +
                ", dailyTransferLimit=" + dailyTransferLimit +
                ", oneTimeTransferLimit=" + oneTimeTransferLimit +
                ", accountBalance=" + accountBalance +
                ", lastTransactionDate='" + lastTransactionDate + '\'' +
                ", currency='" + currency + '\'' +
                "}\n";
    }
}
