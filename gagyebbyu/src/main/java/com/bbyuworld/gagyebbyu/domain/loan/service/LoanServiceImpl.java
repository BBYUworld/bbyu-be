package com.bbyuworld.gagyebbyu.domain.loan.service;

import com.bbyuworld.gagyebbyu.domain.loan.dto.request.LoanRequestDto;
import com.bbyuworld.gagyebbyu.domain.loan.dto.response.LoanResponseDto;
import com.bbyuworld.gagyebbyu.domain.loan.entity.Loan;
import com.bbyuworld.gagyebbyu.domain.loan.repository.LoanRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 대출 관련 작업을 관리하는 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    /**
     * 모든 대출 상품 조회
     *
     * @return 모든 대출 상품을 나타내는 LoanResponseDto 목록
     * @throws RuntimeException 대출 상품이 없을 경우 발생
     */
    public List<LoanResponseDto> getAllLoanProducts() {
        List<Loan> loans = loanRepository.findAll();
        if (loans.isEmpty()) {
            throw new RuntimeException("데이터가 없습니다");
        }
        return loans.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 대출 이름으로 대출 상품 조회
     *
     * @param loanName 검색할 대출의 이름
     * @return 찾은 대출을 나타내는 LoanResponseDto
     * @throws RuntimeException 해당 이름의 대출이 없을 경우 발생
     */
    public List<LoanResponseDto> getLoanByLoanName(String loanName) {
        List<Loan> loans = loanRepository.findByLoanNameContainingIgnoreCase(loanName);
        if (loans == null) {
            throw new RuntimeException("데이터가 없습니다");
        }
        return loans.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 은행 이름으로 대출을 검색
     *
     * @param bankName 대출을 검색할 은행의 이름
     * @return 지정된 은행의 대출을 나타내는 LoanResponseDto 목록
     * @throws RuntimeException 해당 은행의 대출이 없을 경우 발생
     */
    public List<LoanResponseDto> findLoansByBankName(String bankName) {
        List<Loan> loans = loanRepository.findLoansByBankNameContainingIgnoreCase(bankName);
        if (loans.isEmpty()) {
            throw new RuntimeException("데이터가 없습니다");
        }
        return loans.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 지정된 잔액 범위 내의 대출을 검색
     *
     * @param minBalance 검색할 최소 잔액
     * @param maxBalance 검색할 최대 잔액
     * @return 지정된 잔액 범위 내의 대출을 나타내는 LoanResponseDto 목록
     * @throws RuntimeException 지정된 범위 내의 대출이 없을 경우 발생
     */
    public List<LoanResponseDto> searchLoansBesideBalance(Long minBalance, Long maxBalance) {
        List<Loan> loans = loanRepository.findByMinBalanceLessThanEqualAndMaxBalanceGreaterThanEqual(minBalance, maxBalance);
        if (loans.isEmpty()) {
            throw new RuntimeException("데이터가 없습니다");
        }
        return loans.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 이자율 오름차순으로 정렬된 모든 대출을 조회
     *
     * @return 이자율 오름차순으로 정렬된 대출을 나타내는 LoanResponseDto 목록
     * @throws RuntimeException 대출 데이터가 없을 경우 발생
     */
    public List<LoanResponseDto> getOrderedByInterestRate() {
        List<Loan> loans = loanRepository.findAllByOrderByInterestRateAsc();
        if (loans.isEmpty()) {
            throw new RuntimeException("데이터가 없습니다");
        }
        return loans.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * 사용자의 신용정보 출력
     *
     * @return
     */
    @Override
    @RequireJwtToken
    public String getUserRating(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        return user.getRatingName();
    }

    /**
     * Loan 엔티티를 LoanResponseDto로 변환
     *
     * @param loan 변환할 Loan 엔티티
     * @return 변환된 LoanResponseDto
     */
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
}