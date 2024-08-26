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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    /**
     * 사용자의 전체 자산 내역 정보 제공
     *
     * @param userId 유저의 번호
     * @return 해당 유저에 맞는 List 제공
     */
    @Override
    public List<AssetDto> getAssets(Long userId) {
        return assetRepository.findAllByUserIdAndIsHiddenFalse(userId).stream()
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
        return assetRepository.sumAmountByUserIdAndIsHiddenFalse(userId);
    }

    /**
     * 커플에 맞는 전체 자산 정보 제공
     *
     * @param coupleId 커플 번호
     * @return 커플의 모든 자산 정보 List
     */
    @Override
    public List<AssetDto> getCoupleAssets(Long coupleId) {
        return assetRepository.findAllByCoupleIdAndIsHiddenFalse(coupleId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 커플의 전체 자산 총합 제공
     *
     * @param coupleId 커플 번호
     * @return 커플의 자산 총합 sum
     */
    @Override
    public Long getSumCoupleAssets(Long coupleId) {
        return assetRepository.sumAmountByCoupleIdAndIsHiddenFalse(coupleId);
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
     * @param coupleId 커플 번호
     * @param userIds 사용자들의 번호 (userIds 인 List 로 변경 필요)
     */
    @Override
    @Transactional
    public void updateCoupleIdForUsers(Long coupleId, List<Long> userIds) {
        assetRepository.updateCoupleIdForUsers(coupleId, userIds);
    }

    /**
     * asset 과 asset_loan 에 데이터 입력
     *
     * @param assetLoanDto 입력을 위해 받는 LoanDto
     * @return 성공시 true 실패시 false
     */
    @Transactional
    @Override
    public boolean insertNewLoan(AssetLoanDto assetLoanDto) {

        AssetLoan asset = new AssetLoan();

        // Set common Asset properties
        asset.setUserId(assetLoanDto.getUserId());
        asset.setCoupleId(assetLoanDto.getCoupleId());
        asset.setBankName(assetLoanDto.getBankName());
        asset.setBankCode(assetLoanDto.getBankCode());
        asset.setAmount(assetLoanDto.getAmount());
        asset.setIsEnded(assetLoanDto.getIsEnded());
        asset.setIsHidden(assetLoanDto.getIsHidden());

        // Set AssetLoan specific properties
        asset.setLoanName(assetLoanDto.getLoanName());
        asset.setInterestRate(assetLoanDto.getInterestRate());
        asset.setRemainedAmount(assetLoanDto.getRemainedAmount());

        assetRepository.save(asset);

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
    public boolean insertNewAccount(AssetAccountDto assetAccountDto) {

        AssetAccount assetAccount = new AssetAccount();
        assetAccount.setUserId(assetAccountDto.getUserId());
        assetAccount.setCoupleId(assetAccountDto.getCoupleId());
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
    public boolean insertNewCard(AssetCardDto assetCardDto) {

        AssetCard assetCard = new AssetCard();

        assetCard.setUserId(assetCardDto.getUserId());
        assetCard.setCoupleId(assetCardDto.getCoupleId());
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
                .userId(asset.getUserId())
                .coupleId(asset.getCoupleId())
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
