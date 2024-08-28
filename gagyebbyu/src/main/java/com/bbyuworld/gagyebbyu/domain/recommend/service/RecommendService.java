package com.bbyuworld.gagyebbyu.domain.recommend.service;


import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetLoanRepository;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount.AssetAccountService;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetCard.AssetCardService;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetCard.AssetCardServiceImpl;
import com.bbyuworld.gagyebbyu.domain.expense.service.ExpenseService;
import com.bbyuworld.gagyebbyu.domain.loan.entity.RatingName;
import com.bbyuworld.gagyebbyu.domain.loan.service.LoanService;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendDepositRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendLoanRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendSavingsRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendDepositDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendSavingsDto;
import com.bbyuworld.gagyebbyu.domain.user.entity.Gender;
import com.bbyuworld.gagyebbyu.domain.user.entity.Occupation;
import com.bbyuworld.gagyebbyu.domain.user.entity.Region;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.domain.webClient.service.ApiService;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {
    final UserRepository userRepository;
    final LoanService loanService;
    private final ApiService apiService;
    private final AssetLoanRepository assetLoanRepository;
    private final AssetAccountService assetAccountService;
    private final AssetCardService assetCardService;
    private final ExpenseService expenseService;

    public List<Map.Entry<Integer, Double>> getLoanRecommend(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        RecommendLoanRequestDto requestDto = new RecommendLoanRequestDto();
        Long sum = assetLoanRepository.sumRemainedAmountByUser_UserIdAndIsHiddenFalse(userId);
        Long totalDeposit = assetAccountService.getSumAmountByType(userId, AccountType.DEPOSIT);
        Long totalSavings = assetAccountService.getSumAmountByType(userId, AccountType.SAVINGS);
        int cardNum = assetCardService.getCardsNum(userId);
        Long annualSpending = expenseService.getUserExpensesForYear(userId);

        requestDto.setUser_id(userId);
        requestDto.setAge(user.getAge());
        requestDto.setGender(user.getGender() == Gender.F ? 0 : 1);
        requestDto.setRegion(user.getRegion());
        requestDto.setOccupation(user.getOccupation());
        requestDto.setLate_payment(user.getLatePayment() ? 1 : 0);
        requestDto.setFinancial_accident(user.getFinancialAccident());
        requestDto.setAnnual_income(user.getMonthlyIncome()*12);
        requestDto.setDebt(sum);
        if(user.getCreditScore() == null){
            if(user.getRatingName().equals("A")){
                user.setCreditScore(800);
            }else if(user.getRatingName().equals("B")){
                user.setCreditScore(600);
            }else if(user.getRatingName().equals("C")){
                user.setCreditScore(500);
            }else if(user.getRatingName().equals("D"))
                user.setCreditScore(400);
            else {
                user.setCreditScore(300);
            }
        }
        requestDto.setCredit_score(user.getCreditScore());
        requestDto.setAnnual_spending(annualSpending);
        requestDto.setNum_cards(cardNum);
        requestDto.setTotal_deposit(totalDeposit);
        requestDto.setTotal_savings(totalSavings);
        requestDto.setTotal_assets(totalDeposit + totalSavings + sum);


        try {
            return apiService.sendLoanPostRequest("http://localhost:8000/ai/recommend/loan", requestDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create expense", e);
        }
    }
    public List<Map.Entry<Integer, Double>> getDepositRecommend(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        RecommendDepositRequestDto requestDto = new RecommendDepositRequestDto();
        Long sum = assetLoanRepository.sumRemainedAmountByUser_UserIdAndIsHiddenFalse(userId);
        int cardNum = assetCardService.getCardsNum(userId);
        Long annualSpending = expenseService.getUserExpensesForYear(userId);

        requestDto.setUser_id(userId);
        requestDto.setAge(user.getAge());
        requestDto.setGender(user.getGender() == Gender.F ? 0 : 1);
        requestDto.setRegion(user.getRegion());
        requestDto.setOccupation(user.getOccupation());
        requestDto.setLate_payment(user.getLatePayment() ? 1 : 0);
        requestDto.setFinancial_accident(user.getFinancialAccident());
        requestDto.setAnnual_income(user.getMonthlyIncome()*12);
        requestDto.setDebt(sum);
        if(user.getCreditScore() == null){
            if(user.getRatingName().equals("A")){
                user.setCreditScore(800);
            }else if(user.getRatingName().equals("B")){
                user.setCreditScore(600);
            }else if(user.getRatingName().equals("C")){
                user.setCreditScore(500);
            }else if(user.getRatingName().equals("D"))
                user.setCreditScore(400);
            else {
                user.setCreditScore(300);
            }
        }
        requestDto.setCredit_score(user.getCreditScore());
        requestDto.setNum_cards(cardNum);
        requestDto.setAnnual_spending(annualSpending);


        try {
            return apiService.sendDepositPostRequest("http://localhost:8000/ai/recommend/deposit", requestDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create expense", e);
        }
    }
    public List<Map.Entry<Integer, Double>> getSavingsRecommend(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        RecommendSavingsRequestDto requestDto = new RecommendSavingsRequestDto();
        Long sum = assetLoanRepository.sumRemainedAmountByUser_UserIdAndIsHiddenFalse(userId);
        int cardNum = assetCardService.getCardsNum(userId);
        Long annualSpending = expenseService.getUserExpensesForYear(userId);

        requestDto.setUser_id(userId);
        requestDto.setAge(user.getAge());
        requestDto.setGender(user.getGender() == Gender.F ? 0 : 1);
        requestDto.setRegion(user.getRegion());
        requestDto.setOccupation(user.getOccupation());
        requestDto.setLate_payment(user.getLatePayment() ? 1 : 0);
        requestDto.setFinancial_accident(user.getFinancialAccident());
        requestDto.setAnnual_income(user.getMonthlyIncome()*12);
        requestDto.setDebt(sum);
        if(user.getCreditScore() == null){
            if(user.getRatingName().equals("A")){
                user.setCreditScore(800);
            }else if(user.getRatingName().equals("B")){
                user.setCreditScore(600);
            }else if(user.getRatingName().equals("C")){
                user.setCreditScore(500);
            }else if(user.getRatingName().equals("D"))
                user.setCreditScore(400);
            else {
                user.setCreditScore(300);
            }
        }
        requestDto.setCredit_score(user.getCreditScore());
        requestDto.setNum_cards(cardNum);
        requestDto.setAnnual_spending(annualSpending);
        try {
            return apiService.sendSavingsPostRequest("http://localhost:8000/ai/recommend/savings", requestDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create expense", e);
        }
    }
}