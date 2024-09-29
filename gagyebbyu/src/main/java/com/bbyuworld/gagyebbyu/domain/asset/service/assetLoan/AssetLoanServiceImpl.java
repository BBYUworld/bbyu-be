package com.bbyuworld.gagyebbyu.domain.asset.service.assetLoan;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetLoanRepo.AssetLoanRepository;
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
     * @param userId  모든건 사용자 id 기반
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
     * 현재 대출금 정렬 내림차순
     *
     * @param userId 모든건 사용자 id 기반
     * @return 갚을 대출액이 많은 순 정렬
     */
    @Override
    public List<AssetLoanDto> getOrderByAmountDesc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByAmountDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 현재 대출금 정렬 오름차순
     *
     * @param userId 모든건 사용자 id 기반
     * @return 갚을 대출액이 적은 순 정렬
     */
    @Override
    public List<AssetLoanDto> getOrderByAmountAsc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByAmountAsc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 초기 대출금이 많은순 정렬
     *
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @Override
    public List<AssetLoanDto> getOrderByInitialAmountDesc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByInitialAmountDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 초기 대출금이 적은 순 정렬
     *
     * @param userId 모든건 사용자 id 기반
     * @return 대출 리스트
     */
    @Override
    public List<AssetLoanDto> getOrderByInitialAmountAsc(Long userId) {
        return assetLoanRepository.findAllByUser_UserIdAndIsEndedFalseOrderByInitialAmountAsc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 대출 현재 금액 업데이트 및 종료 여부 확인
     *
     * @param assetId 특정 assetId
     * @param amount  현재 대출금
     * @param userId  모든건 사용자 id 기반
     * @return 업데이트된 행의 수
     */
    @Override
    public int updateAmountAndCheckIsEnded(Long assetId, Long amount, Long userId) {
        return assetLoanRepository.updateAmountAndCheckIsEnded(assetId, amount, UserContext.getUserId());
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
    public AssetLoanDto getTargetAssetLoan(Long assetId) {
        return convertToDto(assetLoanRepository.findByAssetId(assetId));
    }

    /**
     * 부부 공통의 대출 반환
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
     * 사용자의 현재 총 대출 금액 조회
     *
     * @param userId 사용자 id
     * @return 현재 총 대출 금액
     */
    @Override
    public Long getUserTotalAmount(Long userId) {
        return assetLoanRepository.findTotalAmountByUser_UserId(userId);
    }

    private AssetLoanDto convertToDto(AssetLoan assetLoan) {
        return AssetLoanDto.builder()
                .assetId(assetLoan.getAssetId())
                .userId(assetLoan.getUser().getUserId())
                .coupleId(assetLoan.getCouple() != null ? assetLoan.getCouple().getCoupleId() : null)
                .user1Id(assetLoan.getCouple() != null ? assetLoan.getCouple().getUser1().getUserId() : null)
                .user2Id(assetLoan.getCouple() != null ? assetLoan.getCouple().getUser2().getUserId() : null)
                .user1Name(assetLoan.getCouple() != null ? assetLoan.getCouple().getUser1().getName() : null)
                .user2Name(assetLoan.getCouple() != null ? assetLoan.getCouple().getUser2().getName() : null)
                .type(String.valueOf(assetLoan.getType()))
                .bankName(assetLoan.getBankName())
                .bankCode(assetLoan.getBankCode())
                .amount(assetLoan.getAmount())
                .createdAt(assetLoan.getCreatedAt())
                .updatedAt(assetLoan.getUpdatedAt())
                .isEnded(assetLoan.getIsEnded())
                .build();
    }
}