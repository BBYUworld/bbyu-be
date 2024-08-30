package com.bbyuworld.gagyebbyu.domain.asset.service.assetLoan;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetLoanRepository;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetLoanServiceImpl implements AssetLoanService {

    private final AssetLoanRepository assetLoanRepository;
    private final UserRepository userRepository;
    private final CoupleRepository coupleRepository;

    /**
     * 사용자의 특정 loan 상품을 불러오기 위함
     *
     * @param userId 모든건 사용자 id 기반
     * @param assetId 특정 assetId
     * @return 특정 loan 상품
     */
    @Override
    public Optional<AssetLoanDto> getUserTargetLoan(Long userId, Long assetId) {
        return assetLoanRepository.findByUser_UserIdAndAssetId(userId, assetId)
                .map(this::convertToDto);
    }

    /**
     * 사용자의 id에 맞는 모든 대출 상품 출력
     *
     * @param userId 모든건 사용자 id 기반
     * @return 사용자가 가입한 대출 상품 list
     */
    @Override
    public List<AssetLoanDto> getUserLoans(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalse(userId).stream()
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
    public List<AssetLoanDto> getOrderByRemainAmountDesc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByRemainedAmountDesc(userId).stream()
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
    public List<AssetLoanDto> getOrderByRemainAmountAsc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByRemainedAmountAsc(userId).stream()
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
    public List<AssetLoanDto> getOrderByAmountDesc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByAmountDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 대출금이 적은 순 정렬
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @Override
    public List<AssetLoanDto> getOrderByAmountAsc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByAmountAsc(userId).stream()
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
        return assetLoanRepository.updateRemainedAmountAndCheckIsEnded(assetId, remainedAmount, UserContext.getUserId());
    }

    /**
     * 다 갚은 대출 리스트 출력
     *
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @Override
    public List<AssetLoanDto> getEndedLoans(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedTrue(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssetLoanDto getTargetAssetLoan(Long assetId){
        return convertToDto(assetLoanRepository.findByAssetId(assetId));
    }

    /**
     * 부부 공콩의 대출 반환
     *
     * @param userId 사용자 id
     * @return 사용자와 사용자 부부의 대출 반환
     */
    @Override
    public List<AssetLoanDto> getCoupleAssetLoans(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        Long coupleId = user.getCoupleId();

        return assetLoanRepository.findAllByCouple_CoupleIdOrderByAmountDesc(coupleId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public Long getUserRemainedAmount(Long userId) {
        return assetLoanRepository.sumRemainedAmountByUser_UserIdAndIsHiddenFalse(userId);
    }


    private AssetLoanDto convertToDto(AssetLoan assetLoan) {
        return AssetLoanDto.builder()
                .assetId(assetLoan.getAssetId())
                .userId(assetLoan.getUser().getUserId())
                .coupleId(assetLoan.getCouple() != null ? assetLoan.getCouple().getCoupleId() : null)
                .user1Id(assetLoan.getCouple() != null ? assetLoan.getCouple().getUser1().getUserId() : null)
                .user2Id(assetLoan.getCouple() != null ?assetLoan.getCouple().getUser2().getUserId() : null)
                .user1Name(assetLoan.getCouple() != null ?assetLoan.getCouple().getUser1().getName() : null)
                .user2Name(assetLoan.getCouple() != null ?assetLoan.getCouple().getUser2().getName() : null)
                .type(String.valueOf(assetLoan.getType()))
                .bankName(assetLoan.getBankName())
                .bankCode(assetLoan.getBankCode())
                .amount(assetLoan.getAmount())
                .createdAt(assetLoan.getCreatedAt())
                .updatedAt(assetLoan.getUpdatedAt())
                .isEnded(assetLoan.getIsEnded())
                .isHidden(assetLoan.getIsHidden())
                // AssetLoan 특정 필드
                .loanName(assetLoan.getLoanName())
                .interestRate(assetLoan.getInterestRate())
                .remainedAmount(assetLoan.getRemainedAmount())
                .build();
    }
}