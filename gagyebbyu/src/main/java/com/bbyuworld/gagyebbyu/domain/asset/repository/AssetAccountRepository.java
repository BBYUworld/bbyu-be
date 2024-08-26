package com.bbyuworld.gagyebbyu.domain.asset.repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetAccountRepository extends JpaRepository<AssetAccount, Long> {
    @EntityGraph(value = "Asset.withUser")
    List<AssetAccount> findByUser_UserIdAndIsHiddenFalse(Long userId);

    @EntityGraph(value = "Asset.withUser")
    List<AssetAccount> findByUser_UserIdAndBankNameAndIsHiddenFalse(Long userId, String bankName);

    @EntityGraph(value = "Asset.withUser")
    List<AssetAccount> findByUser_UserIdAndAccountTypeAndIsHiddenFalse(Long userId, AccountType accountType);

    @EntityGraph(value = "Asset.withUser")
    List<AssetAccount> findByUser_UserIdAndBankNameAndAccountTypeAndIsHiddenFalse(Long userId, String bankName, AccountType accountType);

    @EntityGraph(value = "Asset.withUser")
    Optional<AssetAccount> findFirstByUser_UserIdAndIsHiddenFalseOrderByAmountDesc(Long userId);

    
}