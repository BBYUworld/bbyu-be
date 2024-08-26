package com.bbyuworld.gagyebbyu.domain.loan.repository;

import com.bbyuworld.gagyebbyu.domain.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    /**
     * 대출 상품 리스트 가져오기
     * @return loan에 저장된 모든 내용
     */
    List<Loan> findAll();

    /**
     * 대출 이름으로 검색하기
     * @param loanName
     * @return 해당 이름과 맞는 loan 정보
     */
    Loan findByLoanNameContainingIgnoreCase(String loanName);

    /**
     * 은행별 대출 정보 가져오기
     * @param bankName
     * @return 은행의 모든 loan 내용
     */
    List<Loan> findLoansByBankName(String bankName);

    /**
     * 대출 금액 범위로 검색
     * @param minBalance
     * @param maxBalance
     * @return 범위 내의 Loan List
     */
    List<Loan> findByMinBalanceLessThanEqualAndMaxBalanceGreaterThanEqual(Long minBalance, Long maxBalance);

    /**
     * 이자율 오름차순으로 정렬
     * @return Loan의 interestRate기준 정렬
     */
    List<Loan> findAllByOrderByInterestRateAsc();

    /**
     * 사용자의 신용등급 가져오기
     * @param User.ratingName
     * @return user의 신용등급 반환
     */


}
