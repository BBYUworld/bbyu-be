package com.bbyuworld.gagyebbyu.global.api.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDemandDepositAccountDto {
    private String bankCode;
    private String accountNo;
    @JsonProperty("currency")
    private Currency currency;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Currency{
        private String currency;
        private String currencyName;

        @Override
        public String toString() {
            return "Currency{" +
                    "currency='" + currency + '\'' +
                    ", currencyName='" + currencyName + '\'' +
                    "}\n";
        }
    }

    @Override
    public String toString() {
        return "CreateDemandDepositAccountDto{" +
                "bankCode='" + bankCode + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", currency=" + currency +
                "}\n";
    }
}
