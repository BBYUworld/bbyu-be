package com.bbyuworld.gagyebbyu.domain.asset.service.assetAccount;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetAccountDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.Asset;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetAccountRepository;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetAccountServiceImpl implements AssetAccountService {
    private final AssetAccountRepository assetAccountRepository;
    private final AssetRepository assetRepository;

    @Override
    public List<AssetAccountDto> getAllAssetAccounts(Long userId) {
        return assetAccountRepository.findByUser_UserIdAndIsHiddenFalse(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> getAssetAccountsByBank(Long userId, String bankName) {
        return assetAccountRepository.findByUser_UserIdAndBankNameAndIsHiddenFalse(userId, bankName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> getAssetAccountsByType(Long userId, AccountType accountType) {
        return assetAccountRepository.findByUser_UserIdAndAccountTypeAndIsHiddenFalse(userId, accountType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetAccountDto> getAssetAccountByBankAndType(Long userId, String bankName, AccountType accountType) {
        return assetAccountRepository.findByUser_UserIdAndBankNameAndAccountTypeAndIsHiddenFalse(userId, bankName, accountType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssetAccountDto getMaxAssetAccount(Long userId) {
        return assetAccountRepository.findFirstByUser_UserIdAndIsHiddenFalseOrderByAmountDesc(userId)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public Long getCountByHidden(Long userId) {
        return assetAccountRepository.countByUser_UserIdAndIsHiddenFalse(userId);
    }

    private AssetAccountDto convertToDto(AssetAccount assetAccount) {

        return AssetAccountDto.builder()
                .assetId(assetAccount.getAssetId())
                .userId(assetAccount.getUser().getUserId())
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