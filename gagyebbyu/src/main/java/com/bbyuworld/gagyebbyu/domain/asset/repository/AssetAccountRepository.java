package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetAccountRepository extends JpaRepository<AssetAccount, Long> {
    List<AssetAccount> findByUserIdAndIsHiddenFalse(Long userId);

    List<AssetAccount> findByUserIdAndBankNameAndIsHiddenFalse(Long userId, String bankName);

    List<AssetAccount> findByUserIdAndAccountTypeAndIsHiddenFalse(Long userId, AccountType accountType);

    List<AssetAccount> findByUserIdAndBankNameAndAccountTypeAndIsHiddenFalse(Long userId, String bankName, AccountType accountType);

    Optional<AssetAccount> findFirstByUserIdAndIsHiddenFalseOrderByAmountDesc(Long userId);

    long countByUserIdAndIsHiddenFalse(Long userId);

    Optional<AssetAccount> findByUserIdAndAssetIdAndIsHiddenFalse(Long userId, Long assetId);
}