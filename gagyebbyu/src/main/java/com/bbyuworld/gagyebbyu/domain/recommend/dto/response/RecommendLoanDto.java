package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public final class RecommendLoanDto {
    private Long loan_id;
    private double pred;
}
