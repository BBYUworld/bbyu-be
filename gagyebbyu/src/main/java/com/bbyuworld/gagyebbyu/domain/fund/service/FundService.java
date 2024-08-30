package com.bbyuworld.gagyebbyu.domain.fund.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetAccountRepository;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.fund.dto.request.FundCreateDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.request.FundTransactionCreateDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundOverViewDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundStatusDto;
import com.bbyuworld.gagyebbyu.domain.fund.dto.response.FundTransactionDto;
import com.bbyuworld.gagyebbyu.domain.fund.entity.Fund;
import com.bbyuworld.gagyebbyu.domain.fund.entity.TransactionType;
import com.bbyuworld.gagyebbyu.domain.fund.repository.FundRepository;
import com.bbyuworld.gagyebbyu.domain.fund.repository.FundTransactionRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.domain.user.service.AccountService;
import com.bbyuworld.gagyebbyu.domain.webClient.service.ApiService;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.DemandBalanceDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.DepositDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.WithdrawalDto;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.BadRequestException;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import com.bbyuworld.gagyebbyu.global.util.ApiPost;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundService {

	private final ApiService apiService;
	private final AssetAccountRepository assetAccountRepository;
	@Value("${ssafy.api_key}")
	private String apiKey;
	private final ApiPost apiPost;
	private final FundRepository fundRepository;
	private final CoupleRepository coupleRepository;
	private final UserRepository userRepository;
	private final FundTransactionRepository fundTransactionRepository;
	private final FundConditionService fundConditionService;
	private final AccountService accountService;

	public FundOverViewDto getFund(long coupleId) {
		Fund fund = fundRepository.findByCouple_CoupleIdAndIsEndedIsFalseAndIsDeletedFalse(coupleId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.FUND_NOT_EXIST));

		return FundOverViewDto.from(fund);
	}

	@Transactional
	public void createFund(long userId, long coupleId, FundCreateDto fundCreateDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		Couple couple = coupleRepository.findById(coupleId)
			.orElseThrow(() -> new DataNotFoundException((ErrorCode.COUPLE_NOT_FOUND)));

		fundRepository.save(fundCreateDto.toEntity(couple));
	}

	@Transactional
	public void deleteFund(long fundId) {
		Fund fund = fundRepository.findById(fundId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.FUND_NOT_FOUND));

		fund.deleteFund();

	}

	@Transactional
	public FundStatusDto createFundTransaction(long fundId, long userId,
		FundTransactionCreateDto fundTransactionCreateDto) {

		Fund fund = fundRepository.findById(fundId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.FUND_NOT_FOUND));

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

		AssetAccount account = assetAccountRepository.findAssetAccountByAccountNumberAndUser_UserId(
			fundTransactionCreateDto.getAccountNo(), userId);

		if (fundTransactionCreateDto.getType() == TransactionType.PLUS) {
			if (account.getDailyTransferLimit() < fundTransactionCreateDto.getAmount()) {
				throw new BadRequestException(ErrorCode.ONE_TIME_TRANSFER_LIMIT);
			}

			DemandBalanceDto demandBalanceDto = accountService.findDemandBalance(userId,
				fundTransactionCreateDto.getAccountNo());

			fundConditionService.accountIsExist(demandBalanceDto);

			fundConditionService.chargeIsAvailable(demandBalanceDto.getAccountBalance(),
				fundTransactionCreateDto.getAmount());

			WithdrawalDto withdrawalDto = accountService.updateWithdrawal(userId,
				fundTransactionCreateDto.getAccountNo(),
				String.valueOf(fundTransactionCreateDto.getAmount()), "(펀딩):충전");

			account.setAmount(account.getAmount() - fundTransactionCreateDto.getAmount());
		}

		if (fundTransactionCreateDto.getType() == TransactionType.MINUS) {
			fundConditionService.isExceededEmergency(fund.getEmergency());

			DepositDto depositDto = accountService.updateDeposit(userId, fundTransactionCreateDto.getAccountNo(),
				String.valueOf(fundTransactionCreateDto.getAmount()), "(펀딩):입금");

			account.setAmount(account.getAmount() + fundTransactionCreateDto.getAmount());
		}

		fundTransactionRepository.save(fundTransactionCreateDto.toEntity(user, fund));

		fund.updateFund(fundTransactionCreateDto.getAmount(), fundTransactionCreateDto.getType());

		fundConditionService.isAchievedFund(fund);

		return FundStatusDto.from(fund);

	}

	public List<FundTransactionDto> getFundTransaction(long fundId) {
		return fundTransactionRepository.findByFund_FundIdOrderByDateDesc(fundId)
			.stream()
			.map(FundTransactionDto::from)
			.collect(Collectors.toList());
	}
}
