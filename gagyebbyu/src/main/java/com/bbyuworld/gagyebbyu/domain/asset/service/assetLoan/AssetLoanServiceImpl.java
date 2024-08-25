package com.bbyuworld.gagyebbyu.domain.asset.service.assetLoan;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetLoanServiceImpl implements AssetLoanService {

    private final AssetLoanRepository assetLoanRepository;

    /**
     * 사용자의 특정 loan 상품을 불러오기 위함
     *
     * @param userId 모든건 사용자 id 기반
     * @param assetId 특정 assetId
     * @return 특정 loan 상품
     */
    @Override
    public Optional<AssetLoanDto> postUserTargetLoan(Long userId, Long assetId) {
        return assetLoanRepository.findByUserIdAndAssetId(userId, assetId)
                .map(this::convertToDto);
    }

    /**
     * 사용자의 id에 맞는 모든 대출 상품 출력
     *
     * @param userId 모든건 사용자 id 기반
     * @return 사용자가 가입한 대출 상품 list
     */
    @Override
    public List<AssetLoanDto> postUserLoans(Long userId) {
        return assetLoanRepository.findAllByUserIdAndIsEndedFalse(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 남은 대출금 정렬 내림차순
     *
     * @param userId 모든건 사용자 id 기반
     * @return 갚을 대출액이 많은 순 정렬
     */
    @Override
    public List<AssetLoanDto> postOrderByRemainAmountDesc(Long userId) {
        return assetLoanRepository.findAllByUserIdAndIsEndedFalseOrderByRemainedAmountDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 남은 대출금 정렬 오름차순
     *
     * @param userId 모든건 사용자 id 기반
     * @return 갚을 대출액이 적은ㄴ 순 정렬
     */
    @Override
    public List<AssetLoanDto> postOrderByRemainAmountAsc(Long userId) {
        return assetLoanRepository.findAllByUserIdAndIsEndedFalseOrderByRemainedAmountAsc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 대출금이 많은순 정렬
     *
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @Override
    public List<AssetLoanDto> postOrderByAmountDesc(Long userId) {
        return assetLoanRepository.findAllByUserIdAndIsEndedFalseOrderByAmountDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 대출금이 적은 순 정렬
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @Override
    public List<AssetLoanDto> postOrderByAmountAsc(Long userId) {
        return assetLoanRepository.findAllByUserIdAndIsEndedFalseOrderByAmountAsc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 대출 다 갚을 경우 hidden 변경
     *
     * @param assetId 특정 assetId
     * @param remainedAmount 남은 대출금
     * @param userId 모든건 사용자 id 기반
     * @return hidden 을 변경
     */
    @Override
    public int isEndedUpdate(Long assetId, Long remainedAmount, Long userId) {
        return assetLoanRepository.updateRemainedAmountAndCheckIsEnded(assetId, remainedAmount, userId);
    }

    /**
     * 다 갚은 대출 리스트 출력
     *
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @Override
    public List<AssetLoanDto> postEndedLoans(Long userId) {
        return assetLoanRepository.findAllByUserIdAndIsEndedTrue(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AssetLoanDto convertToDto(AssetLoan assetLoan) {
        return AssetLoanDto.builder()
                .assetId(assetLoan.getAssetId())
                .userId(assetLoan.getUserId())
                .bankName(assetLoan.getBankName())
                .amount(assetLoan.getAmount())
                .interestRate(assetLoan.getInterestRate())
                .isHidden(assetLoan.getIsHidden())
                .loanName(assetLoan.getLoanName())
                .remainedAmount(assetLoan.getRemainedAmount())
                .build();
    }
}