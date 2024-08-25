package com.bbyuworld.gagyebbyu.global.api.asset;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDemandDepositAccountDto {
    private String bankCode;
    private String accountNo;
    private Currency currency;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class Currency{
        private String currency;
        private String currencyName;
    }
}
