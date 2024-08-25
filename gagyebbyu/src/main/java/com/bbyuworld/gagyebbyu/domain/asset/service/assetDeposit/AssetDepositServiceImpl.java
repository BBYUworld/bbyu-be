package com.bbyuworld.gagyebbyu.domain.asset.service.assetDeposit;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDepositDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetDeposit;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetDepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetDepositServiceImpl implements AssetDepositService {
    private final AssetDepositRepository assetDepositRepository;

    @Override
    public List<AssetDepositDto> getAllAssetDeposit() {
        return assetDepositRepository.findAllByHiddenFalse().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDepositDto> getAssetDepositByBank(String bank) {
        return assetDepositRepository.findAllByBankAndHiddenFalse(bank).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDepositDto> getAssetDepositByType(String type) {
        return assetDepositRepository.findAllByTypeAndHiddenFalse(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDepositDto> getAssetDepositByBankAndType(String bank, String type) {
        return assetDepositRepository.findAllByBankAndTypeAndHiddenFalse(bank, type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDepositDto getMaxAssetDeposit() {
        AssetDeposit maxAssetDeposit = assetDepositRepository.findAssetDepositWithMaxAmountAndHiddenFalse();
        return maxAssetDeposit != null ? convertToDto(maxAssetDeposit) : null;
    }

    @Override
    public Long getCountByHidden() {
        return assetDepositRepository.countByHiddenFalse();
    }

    @Override
    public AssetDepositDto getAssetDepositByAssetId(Long assetId) {
        AssetDeposit assetDeposit = assetDepositRepository.findByAssetIdAndHiddenFalse(assetId);
        return assetDeposit != null ? convertToDto(assetDeposit) : null;
    }

    private AssetDepositDto convertToDto(AssetDeposit assetDeposit) {
        return AssetDepositDto.builder()
                .assetDepositId(assetDeposit.getAssetDepositId())
                .assetId(assetDeposit.getAssetId())
                .amount(assetDeposit.getAmount())
                .bank(assetDeposit.getBank())
                .type(assetDeposit.getType())
                .number(assetDeposit.getNumber())
                .hidden(assetDeposit.isHidden())
                .build();
    }

}