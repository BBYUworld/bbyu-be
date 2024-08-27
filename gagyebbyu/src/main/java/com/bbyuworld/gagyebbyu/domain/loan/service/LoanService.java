package com.bbyuworld.gagyebbyu.domain.loan.service;

import com.bbyuworld.gagyebbyu.domain.loan.dto.response.LoanResponseDto;
import java.util.List;

public interface LoanService {

    List<LoanResponseDto> getAllLoanProducts();

    List<LoanResponseDto>  getLoanByLoanName(String loanName);

    List<LoanResponseDto> findLoansByBankName(String bankName);

    List<LoanResponseDto> searchLoansBesideBalance(Long minBalance, Long maxBalance);

    List<LoanResponseDto> getOrderedByInterestRate();

    String getUserRating(Long userId);
}