package com.bbyuworld.gagyebbyu.domain.recommend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import com.bbyuworld.gagyebbyu.domain.asset.enums.LoanType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetRepository;
import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetLoanRepository;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount.AssetAccountService;
import com.bbyuworld.gagyebbyu.domain.asset.service.assetCard.AssetCardService;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.service.ExpenseService;
import com.bbyuworld.gagyebbyu.domain.loan.dto.response.LoanResponseDto;
import com.bbyuworld.gagyebbyu.domain.loan.entity.Loan;
import com.bbyuworld.gagyebbyu.domain.loan.repository.LoanRepository;
import com.bbyuworld.gagyebbyu.domain.loan.service.LoanService;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendCompareRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendDepositRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendLoanRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.request.RecommendSavingsRequestDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.DepositDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendCompareDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendDepositDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendLoanDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendResponseDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendSavingsDto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendUser1Dto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.RecommendUser2Dto;
import com.bbyuworld.gagyebbyu.domain.recommend.dto.response.SavingsDto;
import com.bbyuworld.gagyebbyu.domain.recommend.repository.DepositRepository;
import com.bbyuworld.gagyebbyu.domain.recommend.repository.SavingsRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.Gender;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.domain.webClient.service.ApiService;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
	private final SavingsRepository savingsRepository;
	private final DepositRepository depositRepository;
	private final LoanRepository loanRepository;
	private final AssetRepository assetRepository;

	public List<RecommendLoanDto> getLoanRecommend(long userId) {
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
		requestDto.setAnnual_income(user.getMonthlyIncome() * 12);
		requestDto.setDebt(sum);
		if (user.getCreditScore() == null) {
			if (user.getRatingName().equals("A")) {
				user.setCreditScore(800);
			} else if (user.getRatingName().equals("B")) {
				user.setCreditScore(600);
			} else if (user.getRatingName().equals("C")) {
				user.setCreditScore(500);
			} else if (user.getRatingName().equals("D"))
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
			List<RecommendLoanDto> results = new ArrayList<>();
			List<Map.Entry<Integer, Double>> recommmendLoanDtos = apiService.sendLoanPostRequest(
				"http://3.39.19.140:8001/ai/recommend/loan", requestDto);
			for (Map.Entry<Integer, Double> recommendLoanDto : recommmendLoanDtos) {
				LoanResponseDto loanResponseDto = loanRepository.findById(recommendLoanDto.getKey().longValue())
					.map(LoanResponseDto::from)
					.orElseThrow(() -> new DataNotFoundException(ErrorCode.LOAN_NOT_FOUND));
				results.add(new RecommendLoanDto(recommendLoanDto.getKey().longValue(), recommendLoanDto.getValue(),
					loanResponseDto));
			}
			return results;

		} catch (Exception e) {
			throw new RuntimeException("Failed to create expense", e);
		}
	}

	public List<RecommendDepositDto> getDepositRecommend(long userId) {
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
		requestDto.setAnnual_income(user.getMonthlyIncome() * 12);
		if(sum == null)
			requestDto.setDebt(0);
		else
			requestDto.setDebt(sum);
		if (user.getCreditScore() == null) {
			if (user.getRatingName().equals("A")) {
				user.setCreditScore(800);
			} else if (user.getRatingName().equals("B")) {
				user.setCreditScore(600);
			} else if (user.getRatingName().equals("C")) {
				user.setCreditScore(500);
			} else if (user.getRatingName().equals("D"))
				user.setCreditScore(400);
			else {
				user.setCreditScore(300);
			}
		}
		requestDto.setCredit_score(user.getCreditScore());
		requestDto.setNum_cards(cardNum);
		requestDto.setAnnual_spending(annualSpending);

		try {
			List<RecommendDepositDto> results = new ArrayList<>();

			List<Map.Entry<Integer, Double>> recommendDepositDtos = apiService.sendDepositPostRequest(
				"http://3.39.19.140:8001/ai/recommend/deposit", requestDto);

			for (Map.Entry<Integer, Double> entry : recommendDepositDtos) {
				Integer key = entry.getKey();
				Double value = entry.getValue();

				DepositDto depositDto = depositRepository.findById(key.longValue())
					.map(DepositDto::from)
					.orElseThrow(() -> new DataNotFoundException(ErrorCode.DEPOSIT_NOT_FOUND));
				System.out.println("Recommend Deposit Dto = " + depositDto);
				results.add(new RecommendDepositDto(key.longValue(), value, depositDto));
			}

			return results;

		} catch (Exception e) {
			throw new RuntimeException("Failed to create expense", e);
		}
	}

	public List<RecommendSavingsDto> getSavingsRecommend(long userId) {
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
		requestDto.setAnnual_income(user.getMonthlyIncome() * 12);
		if(sum == null)
			requestDto.setDebt(0);
		else
			requestDto.setDebt(sum);
		if (user.getCreditScore() == null) {
			if (user.getRatingName().equals("A")) {
				user.setCreditScore(800);
			} else if (user.getRatingName().equals("B")) {
				user.setCreditScore(600);
			} else if (user.getRatingName().equals("C")) {
				user.setCreditScore(500);
			} else if (user.getRatingName().equals("D"))
				user.setCreditScore(400);
			else {
				user.setCreditScore(300);
			}
		}
		requestDto.setCredit_score(user.getCreditScore());
		requestDto.setNum_cards(cardNum);
		requestDto.setAnnual_spending(annualSpending);
		try {
			List<RecommendSavingsDto> results = new ArrayList<>();

			List<Map.Entry<Integer, Double>> recommendDepositDtos = apiService.sendSavingsPostRequest(
				"http://3.39.19.140:8001/ai/recommend/savings", requestDto);

			for (Map.Entry<Integer, Double> entry : recommendDepositDtos) {
				Integer key = entry.getKey();
				Double value = entry.getValue();

				SavingsDto savingsDto = savingsRepository.findById(key.longValue())
					.map(SavingsDto::from)
					.orElseThrow(() -> new DataNotFoundException(ErrorCode.SAVINGS_NOT_FOUND));
				System.out.println("Recommend Deposit Dto = " + savingsDto);
				results.add(new RecommendSavingsDto(key.longValue(), value, savingsDto));
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeException("Failed to create expense", e);
		}
	}

	public RecommendResponseDto getCompareRecommend(long userId, long money) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(user.getCoupleId())
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

		User user1 = null;
		User user2 = null;
		//두 명의 사용자
		if (couple.getUser1().getGender() == Gender.M) {
			user1 = userRepository.findById(couple.getUser1().getUserId())
				.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
			user2 = userRepository.findById(couple.getUser2().getUserId())
				.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
		} else {
			user1 = userRepository.findById(couple.getUser2().getUserId())
				.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
			user2 = userRepository.findById(couple.getUser1().getUserId())
				.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
		}
		
		RecommendCompareRequestDto compareRequestDto = new RecommendCompareRequestDto();
		List<AssetLoan> maleList = assetLoanRepository.findAllByUser_UserIdAndIsHiddenFalse(user1.getUserId());
		List<AssetLoan> femaleList = assetLoanRepository.findAllByUser_UserIdAndIsHiddenFalse(user2.getUserId());
		long maleSum=0;
		long femaleSum=0;

		for(AssetLoan assetLoan : maleList) {
			maleSum = (long) (assetLoan.getAmount()*(1 + (0.01* Double.parseDouble(String.valueOf(assetLoan.getInterestRate())) + 0.0075) * 5));
		}
		for(AssetLoan assetLoan : femaleList) {
			femaleSum = (long) (assetLoan.getAmount()*(1 + (0.01* Double.parseDouble(String.valueOf(assetLoan.getInterestRate())) + 0.0075) * 5));
		}
		maleSum/=5;
		femaleSum/=5;

		int maleCreditScore = 0;
		int femaleCreditScore = 0;

		String maleGrade = user1.getRatingName();
		String femaleGrade = user2.getRatingName();

		if (maleGrade.equals("A")) {
			maleCreditScore = 850;
		}
		if (femaleGrade.equals("A")) {
			femaleCreditScore = 850;
		}
		if (maleGrade.equals("B")) {
			maleCreditScore = 700;
		}
		if (femaleGrade.equals("B")) {
			femaleCreditScore = 700;
		}
		if (maleGrade.equals("C")) {
			maleCreditScore = 600;
		}
		if (femaleGrade.equals("C")) {
			femaleCreditScore = 600;
		}
		if (maleGrade.equals("D")) {
			maleCreditScore = 450;
		}
		if (femaleGrade.equals("D")) {
			femaleCreditScore = 450;
		}
		if (maleGrade.equals("E")) {
			maleCreditScore = 300;
		}
		if (femaleGrade.equals("E")) {
			femaleCreditScore = 300;
		}

		// 필요한 데이터를 RequestDto에 설정
		compareRequestDto.setMale_income(user1.getMonthlyIncome() * 12); // 남성 연 소득 설정
		compareRequestDto.setFemale_income(user2.getMonthlyIncome() * 12); // 여성 연 소득 설정
		compareRequestDto.setMale_debt(maleSum); // 남성 부채 설정
		compareRequestDto.setFemale_debt(femaleSum); // 여성 부채 설정
		compareRequestDto.setTarget_amount(money);
		compareRequestDto.setStress_rate(0.0075);
		compareRequestDto.setMale_credit_score(maleCreditScore);
		compareRequestDto.setFemale_credit_score(femaleCreditScore);

		Long male_mortgage = assetLoanRepository.findTotalAmountByLoanTypeAndUser_UserId(LoanType.valueOf("주택담보대출"), user1.getUserId());
		Long female_mortgage = assetLoanRepository.findTotalAmountByLoanTypeAndUser_UserId(LoanType.valueOf("주택담보대출"), user2.getUserId());
		compareRequestDto.setMortgage_loan_amount_female(female_mortgage);
		compareRequestDto.setMortgage_loan_amount_male(male_mortgage);

		// Python 서버로 POST 요청 전송
		try {
			List<RecommendCompareDto> responseDto = apiService.sendComparePostRequest(
				"http://3.39.19.140:8002/ai/recommend/compare",
				compareRequestDto);

			List<RecommendDto> coupleLoanRecommends = new ArrayList<>();

			for (RecommendCompareDto dto : responseDto) {
				Loan user1Loan = loanRepository.findById((long)dto.getMale_loan_id())
					.orElseThrow(() -> new DataNotFoundException(ErrorCode.LOAN_NOT_FOUND));
				Loan user2Loan = loanRepository.findById((long)dto.getMale_loan_id())
					.orElseThrow(() -> new DataNotFoundException(ErrorCode.LOAN_NOT_FOUND));

				coupleLoanRecommends.add(
					new RecommendDto(dto.getTotal_payment(), RecommendUser1Dto.from(user1Loan, dto),
						RecommendUser2Dto.from(user2Loan, dto)));
			}

			return RecommendResponseDto.from(user1, user2, coupleLoanRecommends);

		} catch (Exception e) {
			throw new RuntimeException("Failed to get loan recommendation", e);
		}
	}
}
