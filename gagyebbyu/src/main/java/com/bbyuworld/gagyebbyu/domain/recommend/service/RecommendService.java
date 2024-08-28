package com.bbyuworld.gagyebbyu.domain.recommend.service;


import com.bbyuworld.gagyebbyu.domain.loan.service.LoanService;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendLoanRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendDepositDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendLoanDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendSavingsDto;
import com.bbyuworld.gagyebbyu.domain.user.entity.Gender;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {
    final UserRepository userRepository;
    final LoanService loanService;

    public RecommendLoanDto getLoanRecommend(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        RecommendLoanRequestDto requestDto = new RecommendLoanRequestDto();

        requestDto.setUser_id(userId);
        requestDto.setAge(user.getAge());
        requestDto.setGender(user.getGender() == Gender.F ? 0 : 1);
        requestDto.setRegion(user.getRegion());
        requestDto.setOccupation(user.getOccupation());
        requestDto.setLate_payment(user.getLatePayment() ? 1 : 0);
        requestDto.setFinancial_accident(user.getFinancialAccident());
        requestDto.setAnnual_income(user.getMonthlyIncome()*12);

        try {
            return apiService.sendPostRequest("http://localhost:8000/ai/recommend", requestDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create expense", e);
        }


        //RecommendRequestDto에 원하는 정보를 담기


        //담은 정보를 python ai 서버에 날리기(오빠가 만든 api)

        //파이썬 api response를 RecommendLoanDto에 담기
        return null;
    }
    public RecommendDepositDto getDepositRecommend(long userId){

        return null;
    }
    public RecommendSavingsDto getSavingsRecommend(long userId){

        return null;
    }
}
