package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.dto.AssetDepositDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetDepositRepository extends JpaRepository<AssetDeposit, Long> {
    List<AssetDeposit> findAllByHiddenFalse();
    List<AssetDeposit> findAllByBankAndHiddenFalse(String bank);
    List<AssetDeposit> findAllByTypeAndHiddenFalse(String type);
    List<AssetDeposit> findAllByBankAndTypeAndHiddenFalse(String bank, String type);

    @Query("SELECT ad FROM AssetDeposit ad WHERE ad.amount = (SELECT MAX(ad2.amount) FROM AssetDeposit ad2 WHERE ad2.hidden = false) AND ad.hidden = false")
    AssetDeposit findAssetDepositWithMaxAmountAndHiddenFalse();

    long countByHiddenFalse();
    AssetDeposit findByAssetIdAndHiddenFalse(Long assetId);
}
