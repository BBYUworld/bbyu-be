package com.bbyuworld.gagyebbyu.global.api.demanddeposit;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandDepositDto {
    private String accountTypeUniqueNo;
    private String bankCode;
    private String bankName;
    private String accountTypeCode;
    private String accountTypeName;
    private String accountName;
    private String accountDescription;
    private String accountType;

    @Override
    public String toString() {
        return "DemandDepositDto{" +
                "accountTypeUniqueNo='" + accountTypeUniqueNo + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountTypeCode='" + accountTypeCode + '\'' +
                ", accountTypeName='" + accountTypeName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountDescription='" + accountDescription + '\'' +
                ", accountType='" + accountType + '\'' +
                "}\n";
    }
}
