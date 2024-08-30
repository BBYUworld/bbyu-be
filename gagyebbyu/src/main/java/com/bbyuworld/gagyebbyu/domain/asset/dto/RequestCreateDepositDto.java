package com.bbyuworld.gagyebbyu.domain.asset.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCreateDepositDto {
    private String accountNo;
    private String amount;
    private String accountTypeUniqueNo;
}
