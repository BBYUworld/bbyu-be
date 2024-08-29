package com.bbyuworld.gagyebbyu.domain.recommend.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class RecommendCompareDto {
    private int male_loan_id;
    private double male_interest_rate;
    private int male_loan_limit;
    private int male_loan_term_months;
    private int male_credit_score_requirement;

    private int female_loan_id;
    private double female_interest_rate;
    private int female_loan_limit;
    private int female_loan_term_months;
    private int female_credit_score_requirement;

    private double male_ratio;
    private double female_ratio;
    private double total_payment;
    private double male_dsr;
    private double female_dsr;
    private double male_stress_dsr;
    private double female_stress_dsr;
}