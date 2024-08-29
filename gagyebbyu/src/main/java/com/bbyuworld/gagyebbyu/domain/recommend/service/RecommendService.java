package com.bbyuworld.gagyebbyu.domain.recommend.service;


import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetLoanRepository;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount.AssetAccountService;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetCard.AssetCardService;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetCard.AssetCardServiceImpl;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.service.ExpenseService;
import com.bbyuworld.gagyebbyu.domain.loan.entity.RatingName;
import com.bbyuworld.gagyebbyu.domain.loan.service.LoanService;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendCompareRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendDepositRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendLoanRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendSavingsRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendCompareDto;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class RecommendService {
    final UserRepository userRepository;
    final LoanService loanService;
    private final ApiService apiService;
    private final AssetLoanRepository assetLoanRepository;
    private final AssetAccountService assetAccountService;
    private final AssetCardService assetCardService;
    private final ExpenseService expenseService;
    private final CoupleRepository coupleRepository;

    public List<Map.Entry<Integer, Double>> getLoanRecommend(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        RecommendLoanRequestDto requestDto = new RecommendLoanRequestDto();
        Long sum = assetLoanRepository.sumRemainedAmountByUser_UserIdAndIsHiddenFalse(userId);
        Long totalDeposit = assetAccountService.getSumAmountByType(userId, AccountType.DEPOSIT);
        Long totalSavings = assetAccountService.getSumAmountByType(userId, AccountType.SAVINGS);
        int cardNum = assetCardService.getCardsNum(userId);
        Long annualSpending = expenseService.getUserExpensesForYear(userId);
        System.out.println("SUM: "+ sum);
        System.out.println("totalDeposit: "+ totalDeposit);
        System.out.println("cardNum: "+ cardNum);
        System.out.println("annualSpending: "+ annualSpending);

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

        System.out.println("=====================================");
        System.out.println(requestDto.getCredit_score());
        System.out.println(requestDto.getAnnual_spending());
        System.out.println(requestDto.getNum_cards());
        System.out.println(requestDto.getTotal_deposit());
        System.out.println(requestDto.getTotal_savings());
        System.out.println(requestDto.getTotal_assets());
        System.out.println("=====================================");

        // 데이터 유효성 검사
        validateRequestData(requestDto);

        // 요청 데이터 로깅
        log.info("Loan recommendation request data: {}", requestDto);

        try {
            return apiService.sendLoanPostRequest("http://3.39.19.140:8001/ai/recommend/loan", requestDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create expense", e);
        }
    }

    private void validateRequestData(RecommendLoanRequestDto requestDto) {
        if (requestDto.getUser_id() == null || requestDto.getUser_id() <= 0) {
            throw new IllegalArgumentException("Invalid user_id");
        }
        if (requestDto.getAge() == 0 || requestDto.getAge() <= 0) {
            throw new IllegalArgumentException("Invalid age");
        }
        if (requestDto.getGender() == -1 || (requestDto.getGender() != 0 && requestDto.getGender() != 1)) {
            throw new IllegalArgumentException("Invalid gender");
        }
        if (requestDto.getRegion() == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }
        if (requestDto.getOccupation() == null) {
            throw new IllegalArgumentException("Occupation cannot be null");
        }
        if (requestDto.getAnnual_income() == 0 || requestDto.getAnnual_income() < 0) {
            throw new IllegalArgumentException("Invalid annual income");
        }
        if (requestDto.getDebt() == 0 || requestDto.getDebt() < 0) {
            throw new IllegalArgumentException("Invalid debt");
        }
        if (requestDto.getCredit_score() == 0 || requestDto.getCredit_score() < 0 || requestDto.getCredit_score() > 1000) {
            throw new IllegalArgumentException("Invalid credit score");
        }
        if (requestDto.getAnnual_spending() == 0 || requestDto.getAnnual_spending() < 0) {
            throw new IllegalArgumentException("Invalid annual spending");
        }
        if (requestDto.getNum_cards() == 0 || requestDto.getNum_cards() < 0) {
            throw new IllegalArgumentException("Invalid number of cards");
        }
        if (requestDto.getTotal_deposit() == 0 || requestDto.getTotal_deposit() < 0) {
            throw new IllegalArgumentException("Invalid total deposit");
        }
        if (requestDto.getTotal_savings() == 0 || requestDto.getTotal_savings() < 0) {
            throw new IllegalArgumentException("Invalid total savings");
        }
        if (requestDto.getTotal_assets() == 0 || requestDto.getTotal_assets() < 0) {
            throw new IllegalArgumentException("Invalid total assets");
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

    public List<RecommendCompareDto> getCompareRecommend(long userId, long money) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        Couple couple = coupleRepository.findById(user.getCoupleId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        User user1=null;
        User user2=null;
        //두 명의 사용자
        if(couple.getUser1().getGender() == Gender.M) {
            user1 = userRepository.findById(couple.getUser1().getUserId())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
            user2 = userRepository.findById(couple.getUser2().getUserId())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        }
        else{
            user1 = userRepository.findById(couple.getUser2().getUserId())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
            user2 = userRepository.findById(couple.getUser1().getUserId())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        }
        RecommendCompareRequestDto compareRequestDto = new RecommendCompareRequestDto();
        Long maleSum = assetLoanRepository.sumRemainedAmountByUser_UserIdAndIsHiddenFalse(user1.getUserId());
        Long femaleSum = assetLoanRepository.sumRemainedAmountByUser_UserIdAndIsHiddenFalse(user2.getUserId());

        int maleCreditScore = 0;
        int femaleCreditScore = 0;

        String maleGrade = user1.getRatingName();
        String femaleGrade = user2.getRatingName();

        if(maleGrade.equals("A")){
            maleCreditScore=850;
        }
        if(femaleGrade.equals("A")){
            femaleCreditScore=850;
        }
        if(maleGrade.equals("B")){
            maleCreditScore=700;
        }
        if(femaleGrade.equals("B")){
            femaleCreditScore=700;
        }
        if(maleGrade.equals("C")){
            maleCreditScore=600;
        }
        if(femaleGrade.equals("C")){
            femaleCreditScore=600;
        }
        if(maleGrade.equals("D")){
            maleCreditScore=450;
        }
        if(femaleGrade.equals("D")){
            femaleCreditScore=450;
        }
        if(maleGrade.equals("E")){
            maleCreditScore=300;
        }
        if(femaleGrade.equals("E")){
            femaleCreditScore=300;
        }



        // 필요한 데이터를 RequestDto에 설정
        compareRequestDto.setMale_income(user1.getMonthlyIncome() * 12); // 남성 연 소득 설정
        compareRequestDto.setFemale_income(user2.getMonthlyIncome() * 12); // 여성 연 소득 설정
        compareRequestDto.setMale_debt(maleSum); // 남성 부채 설정
        compareRequestDto.setFemale_debt(femaleSum); // 여성 부채 설정
        compareRequestDto.setTarget_amount(money);
        compareRequestDto.setStress_rate(0.0075);
        compareRequestDto.setMale_credit_score(maleCreditScore); // 남성 신용 점수 설정
        compareRequestDto.setFemale_credit_score(femaleCreditScore);

        // 다른 로직이 필요하면 추가적으로 작성 가능

        // Python 서버로 POST 요청 전송
        List<RecommendCompareDto> responseDto;
        try {
            responseDto = apiService.sendComparePostRequest("http://localhost:8000/ai/recommend/compare", compareRequestDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get loan recommendation", e);
        }
        return responseDto;
    }
}
