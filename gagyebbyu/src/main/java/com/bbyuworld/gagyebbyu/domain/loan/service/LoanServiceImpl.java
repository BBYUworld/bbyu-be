package com.bbyuworld.gagyebbyu.domain.loan.service;

import com.bbyuworld.gagyebbyu.domain.loan.dto.request.LoanRequestDto;
import com.bbyuworld.gagyebbyu.domain.loan.dto.response.LoanResponseDto;
import com.bbyuworld.gagyebbyu.domain.loan.entity.Loan;
import com.bbyuworld.gagyebbyu.domain.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    public List<LoanResponseDto> getAllLoanProducts() {
        return loanRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public LoanResponseDto getLoanByLoanName(String loanName) {
        Loan loan = loanRepository.findByLoanNameContainingIgnoreCase(loanName);
        return convertToResponseDto(loan);
    }

    public List<LoanResponseDto> findLoansByBankName(String bankName) {
        return loanRepository.findLoansByBankName(bankName).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDto> searchLoansBesideBalance(Long minBalance, Long maxBalance) {
        return loanRepository.findByMinBalanceLessThanEqualAndMaxBalanceGreaterThanEqual(minBalance, maxBalance)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDto> getOrderedByInterestRate() {
        return loanRepository.findAllByOrderByInterestRateAsc().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private LoanResponseDto convertToResponseDto(Loan loan) {
        return new LoanResponseDto(
                loan.getLoanId(),
                loan.getBankCode(),
                loan.getBankName(),
                loan.getRatingName(),
                loan.getLoanName(),
                loan.getLoanPeriod(),
                loan.getMinBalance(),
                loan.getMaxBalance(),
                loan.getInterestRate(),
                loan.getAccountType(),
                loan.getLoanTypeCode(),
                loan.getLoanTypeName(),
                loan.getRepaymentCode(),
                loan.getRepaymentName(),
                loan.getStartDate()
        );
    }

    private Loan convertToEntity(LoanRequestDto dto) {
        Loan loan = new Loan();
        loan.setBankCode(dto.getBankName());
        loan.setRatingName(dto.getRatingName());
        loan.setLoanName(dto.getLoanName());
        loan.setLoanPeriod(dto.getLoanPeriod());
        loan.setMinBalance(dto.getMinBalance());
        loan.setMaxBalance(dto.getMaxBalance());
        loan.setInterestRate(dto.getInterestRate());
        loan.setAccountType(dto.getAccountType());
        loan.setLoanTypeCode(dto.getLoanTypeCode());
        loan.setRepaymentCode(dto.getRepaymentCode());
        return loan;
    }
}