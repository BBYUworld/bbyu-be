package com.bbyuworld.gagyebbyu.domain.recommend.dto.request;

import com.bbyuworld.gagyebbyu.domain.user.entity.Occupation;
import com.bbyuworld.gagyebbyu.domain.user.entity.Region;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public final class RecommendLoanRequestDto {
    private Long user_id;
    private int gender;
    private Region region;
    private Occupation occupation;
    private int late_payment;
    private long financial_accident;
    private long age;
    private long annual_income;
    private long debt;
    private long credit_score;
    // LOAN serv  -> getUserRating
    private long annual_spending;
    //
    private long num_cards;
    //
    private long total_deposit;
    //
    private long total_savings;
    //
    private long total_assets;
    //
}

