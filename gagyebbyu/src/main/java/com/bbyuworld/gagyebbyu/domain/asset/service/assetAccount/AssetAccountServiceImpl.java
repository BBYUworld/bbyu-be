package com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetAccountRepo.AssetAccountRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetAccountServiceImpl implements AssetAccountService {
    private final AssetAccountRepository assetAccountRepository;
    private final UserRepository userRepository;

    @Override
    public List<AssetAccountDto> getAllAssetAccounts(Long userId) {
        return assetAccountRepository.findByUser_UserIdAndIsHiddenFalseOrderByAmountDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> getAssetAccountsByBank(Long userId, String bankName) {
        return assetAccountRepository.findByUser_UserIdAndBankNameContainingAndIsHiddenFalseOrderByAmountDesc(userId, bankName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> getAssetAccountsByType(Long userId, AccountType accountType) {
        return assetAccountRepository.findByUser_UserIdAndAccountTypeAndIsHiddenFalseOrderByAmountDesc(userId, accountType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> getAssetAccountByBankAndType(Long userId, String bankName, AccountType accountType) {
        return assetAccountRepository.findByUser_UserIdAndBankNameContainingAndAccountTypeAndIsHiddenFalseOrderByAmountDesc(userId, bankName, accountType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<AssetAccountDto> getCoupleAssetAccounts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        return assetAccountRepository.findAllByCouple_CoupleIdAndIsHiddenFalse(user.getCoupleId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 타입에 따른 계좌 총액 출력
     *
     * @param userId user 의 Id
     * @param accountType 타입
     * @return Long 값 반환
     */
    @Override
    public Long getSumAmountByType(Long userId, AccountType accountType) {
        System.out.println("=======================================");
        System.out.println(userId);
        System.out.println(accountType);
        System.out.println("=======================================");
        return assetAccountRepository.sumAmountByUserIdAndAccountType(userId, accountType);
    }

    private AssetAccountDto convertToDto(AssetAccount assetAccount) {
        return AssetAccountDto.builder()
                .assetId(assetAccount.getAssetId())
                .userId(assetAccount.getUser().getUserId())
                .coupleId(assetAccount.getCouple() != null ? assetAccount.getCouple().getCoupleId() : null)
                .type(String.valueOf(assetAccount.getType()))
                .bankName(assetAccount.getBankName())
                .bankCode(assetAccount.getBankCode())
                .amount(assetAccount.getAmount())
                .createdAt(assetAccount.getCreatedAt())
                .updatedAt(assetAccount.getUpdatedAt())
                .isEnded(assetAccount.getIsEnded())
                .isHidden(assetAccount.getIsHidden())
                // AssetAccount 특정 필드
                .accountNumber(assetAccount.getAccountNumber())
                .accountType(assetAccount.getAccountType().name())
                .oneTimeTransferLimit(assetAccount.getOneTimeTransferLimit())
                .dailyTransferLimit(assetAccount.getDailyTransferLimit())
                .maturityDate(assetAccount.getMaturityDate())
                .interestRate(assetAccount.getInterestRate())
                .term(assetAccount.getTerm())
                .build();
    }
}