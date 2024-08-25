package com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetAccountServiceImpl implements AssetAccountService {
    private final AssetAccountRepository assetAccountRepository;

    @Override
    public List<AssetAccountDto> postAllAssetAccounts(Long userId) {
        return assetAccountRepository.findByUserIdAndIsHiddenFalse(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> postAssetAccountsByBank(Long userId, String bankName) {
        return assetAccountRepository.findByUserIdAndBankNameAndIsHiddenFalse(userId, bankName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> postAssetAccountsByType(Long userId, AccountType accountType) {
        return assetAccountRepository.findByUserIdAndAccountTypeAndIsHiddenFalse(userId, accountType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> postAssetAccountByBankAndType(Long userId, String bankName, AccountType accountType) {
        return assetAccountRepository.findByUserIdAndBankNameAndAccountTypeAndIsHiddenFalse(userId, bankName, accountType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssetAccountDto postMaxAssetAccount(Long userId) {
        return assetAccountRepository.findFirstByUserIdAndIsHiddenFalseOrderByAmountDesc(userId)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public Long postCountByHidden(Long userId) {
        return assetAccountRepository.countByUserIdAndIsHiddenFalse(userId);
    }

    private AssetAccountDto convertToDto(AssetAccount assetAccount) {
        return AssetAccountDto.builder()
                .assetId(assetAccount.getAssetId())
                .userId(assetAccount.getUserId())
                .bankName(assetAccount.getBankName())
                .amount(assetAccount.getAmount())
                .accountNumber(assetAccount.getAccountNumber())
                .accountType(assetAccount.getAccountType().name())
                .oneTimeTransferLimit(assetAccount.getOneTimeTransferLimit())
                .dailyTransferLimit(assetAccount.getDailyTransferLimit())
                .maturityDate(assetAccount.getMaturityDate())
                .interestRate(assetAccount.getInterestRate())
                .term(assetAccount.getTerm())
                .isHidden(assetAccount.getIsHidden())
                .build();
    }
}