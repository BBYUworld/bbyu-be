package com.bbyuworld.gagyebbyu.domain.asset.service.asset;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetCardDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetLoanDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetCard;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetLoan;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.enums.CardType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetRepository;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final CoupleRepository coupleRepository;

    /**
     * 사용자의 전체 자산 내역 정보 제공
     *
     * @param userId 유저의 번호
     * @return 해당 유저에 맞는 List 제공
     */
    @Override
    public List<AssetDto> getAssets(Long userId) {
        return assetRepository.findAllByUser_UserIdAndIsHiddenFalse(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 전체 자산 총합 제공
     *
     * @param userId 유저 번호
     * @return 해당 유저에 맞는 sum 값 제공
     */
    @Override
    public Long getSumUserAssets(Long userId) {
        return assetRepository.sumAmountByUser_UserIdAndIsHiddenFalse(userId);
    }

    /**
     * 커플에 맞는 전체 자산 정보 제공
     *
     * @param userId 커플 번호
     * @return 커플의 모든 자산 정보 List
     */
    @Override
    public List<AssetDto> getCoupleAssets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        return assetRepository.findAllByCouple_CoupleIdAndIsHiddenFalse(user.getCoupleId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 커플의 전체 자산 총합 제공
     *
     * @param userId 커플 번호
     * @return 커플의 자산 총합 sum
     */
    @Override
    public Long getSumCoupleAssets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        return assetRepository.sumAmountByCouple_CoupleIdAndIsHiddenFalse(user.getCoupleId());
    }

    /**
     * 자산 보이기 변경 -> is_hidden 변경 -> false: 자산 보임 / true: 자산 숨김& 삭제
     *
     * @param assetId 사용자의 번호
     * @param isHidden 자산 보임 여부
     */
    @Override
    @Transactional
    public void updateAssetVisibility(Long assetId, boolean isHidden) {
        assetRepository.updateAssetVisibility(assetId, isHidden);
    }

    /**
     * 커플 연결됐을 때, update_assetCouple 변경해줘야 함 userid 2개, coupleid 1개
     *
     * @param couple 커플 번호
     * @param user1Id 사용자 번호 1
     * @param user2Id 사용자 번호 2
     */
    @Override
    @Transactional
    public void updateUserAssetsToCouple(@Param("couple") Couple couple, @Param("user1Id") Long user1Id,
                                         @Param("user2Id") Long user2Id) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(user1Id);
        userIds.add(user2Id);
        Long coupleId = couple.getCoupleId();
        assetRepository.updateAssetsByCouple_CoupleId(coupleId, userIds);
    }

    /**
     * asset 과 asset_loan 에 데이터 입력
     *
     * @param assetLoanDto 입력을 위해 받는 LoanDto
     * @return 성공시 true 실패시 false
     */
    @Transactional
    @Override
    public boolean insertNewLoan(AssetLoanDto assetLoanDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        Couple couple = coupleRepository.findById(user.getCoupleId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        AssetLoan assetLoan = new AssetLoan();

        assetLoan.setUser(user);
        assetLoan.setCouple(couple);
        assetLoan.setBankName(assetLoanDto.getBankName());
        assetLoan.setBankCode(assetLoanDto.getBankCode());
        assetLoan.setAmount(assetLoanDto.getAmount());
        assetLoan.setIsEnded(assetLoanDto.getIsEnded());
        assetLoan.setIsHidden(assetLoanDto.getIsHidden());

        // Set AssetLoan specific properties
        assetLoan.setLoanName(assetLoanDto.getLoanName());
        assetLoan.setInterestRate(assetLoanDto.getInterestRate());
        assetLoan.setRemainedAmount(assetLoanDto.getRemainedAmount());

        assetRepository.save(assetLoan);

        return true;
    }

    /**
     * asset 과 asset_account 에 데이터 입력
     *
     * @param assetAccountDto 입력을 위해 받는 Account 정보
     * @return 성공시 true 실패시 false
     */
    @Transactional
    @Override
    public boolean insertNewAccount(AssetAccountDto assetAccountDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        Couple couple = coupleRepository.findById(user.getCoupleId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        AssetAccount assetAccount = new AssetAccount();
        assetAccount.setUser(user);
        assetAccount.setCouple(couple);
        assetAccount.setBankName(assetAccountDto.getBankName());
        assetAccount.setBankCode(assetAccountDto.getBankCode());
        assetAccount.setAmount(assetAccountDto.getAmount());
        assetAccount.setIsEnded(assetAccountDto.getIsEnded());
        assetAccount.setIsHidden(assetAccountDto.getIsHidden());

        assetAccount.setAccountNumber(assetAccountDto.getAccountNumber());
        assetAccount.setAccountType(AccountType.valueOf(assetAccountDto.getAccountType()));
        assetAccount.setOneTimeTransferLimit(assetAccountDto.getOneTimeTransferLimit());
        assetAccount.setDailyTransferLimit(assetAccountDto.getDailyTransferLimit());
        assetAccount.setMaturityDate(assetAccountDto.getMaturityDate());
        assetAccount.setInterestRate(assetAccountDto.getInterestRate());
        assetAccount.setTerm(assetAccountDto.getTerm());

        assetRepository.save(assetAccount);

        return true;
    }

    /**
     * asset 과 asset_card 에 데이터 입력
     *
     * @param assetCardDto 입력을 위해 받는 카드 정보
     * @return 성공시 true 실패시 false
     */
    @Transactional
    @Override
    public boolean insertNewCard(AssetCardDto assetCardDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        Couple couple = coupleRepository.findById(user.getCoupleId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));

        AssetCard assetCard = new AssetCard();

        assetCard.setUser(user);
        assetCard.setCouple(couple);
        assetCard.setBankName(assetCardDto.getBankName());
        assetCard.setBankCode(assetCardDto.getBankCode());
        assetCard.setAmount(assetCardDto.getAmount());
        assetCard.setIsEnded(assetCardDto.getIsEnded());
        assetCard.setIsHidden(assetCardDto.getIsHidden());

        assetCard.setCardNumber(assetCardDto.getCardNumber());
        assetCard.setCardName(assetCardDto.getCardName());
        assetCard.setCardType(CardType.valueOf(assetCardDto.getCardType()));

        assetRepository.save(assetCard);

        return true;
    }

    protected AssetDto convertToDto(Asset asset) {
        return AssetDto.builder()
                .assetId(asset.getAssetId())
                .userId(asset.getUser().getUserId())
                .coupleId(asset.getCouple().getCoupleId())
                .type(asset.getType().name())
                .bankName(asset.getBankName())
                .bankCode(asset.getBankCode())
                .amount(asset.getAmount())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .isEnded(asset.getIsEnded())
                .isHidden(asset.getIsHidden())
                .build();
    }

}
