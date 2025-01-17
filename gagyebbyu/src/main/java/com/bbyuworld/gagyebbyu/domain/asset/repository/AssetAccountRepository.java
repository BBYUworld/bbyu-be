package com.bbyuworld.gagyebbyu.domain.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;

@Repository
public interface AssetAccountRepository extends JpaRepository<AssetAccount, Long>, AssetAccountRepositoryCustom {

	/**
	 * 사용자 특정 계좌 조회
	 *
	 * @param accountNumber 계좌번호
	 * @return 사용자의 모든 활성 계좌 목록
	 */
	@EntityGraph(value = "Asset.withUser")
	AssetAccount findAssetAccountByAccountNumberAndUser_UserId(String accountNumber, long userId);

	/**
	 * 사용자 ID로 숨겨지지 않은 모든 계좌를 조회합니다.
	 * 결과는 금액 내림차순으로 정렬됩니다.
	 *
	 * @param userId 사용자 ID
	 * @return 사용자의 모든 활성 계좌 목록
	 */
	@EntityGraph(value = "Asset.withUser")
	List<AssetAccount> findByUser_UserIdAndIsHiddenFalseOrderByAmountDesc(Long userId);

	/**
	 * 사용자 ID와 은행 이름으로 숨겨지지 않은 계좌를 조회합니다.
	 * 은행 이름은 부분 일치를 지원하며, 결과는 금액 내림차순으로 정렬됩니다.
	 *
	 * @param userId 사용자 ID
	 * @param bankName 은행 이름 (부분 일치 가능)
	 * @return 조건에 맞는 계좌 목록
	 */
	@EntityGraph(value = "Asset.withUser")
	List<AssetAccount> findByUser_UserIdAndBankNameContainingAndIsHiddenFalseOrderByAmountDesc(Long userId,
		String bankName);

	/**
	 * 사용자 ID와 계좌 유형으로 숨겨지지 않은 계좌를 조회합니다.
	 * 결과는 금액 내림차순으로 정렬됩니다.
	 *
	 * @param userId 사용자 ID
	 * @param accountType 계좌 유형
	 * @return 조건에 맞는 계좌 목록
	 */
	@EntityGraph(value = "Asset.withUser")
	List<AssetAccount> findByUser_UserIdAndAccountTypeAndIsHiddenFalseOrderByAmountDesc(Long userId,
		AccountType accountType);

	/**
	 * 사용자 ID, 은행 이름, 계좌 유형으로 숨겨지지 않은 계좌를 조회합니다.
	 * 은행 이름은 부분 일치를 지원하며, 결과는 금액 내림차순으로 정렬됩니다.
	 *
	 * @param userId 사용자 ID
	 * @param bankName 은행 이름 (부분 일치 가능)
	 * @param accountType 계좌 유형
	 * @return 조건에 맞는 계좌 목록
	 */
	@EntityGraph(value = "Asset.withUser")
	List<AssetAccount> findByUser_UserIdAndBankNameContainingAndAccountTypeAndIsHiddenFalseOrderByAmountDesc(
		Long userId, String bankName, AccountType accountType);

	/**
	 * 사용자 ID와 계좌 유형으로 숨겨지지 않은 계좌를 조회합니다.
	 * 결과는 금액 내림차순으로 정렬됩니다.
	 *
	 * @param userId 사용자 ID
	 * @param accountType 계좌 유형
	 * @return 조건에 맞는 계좌 목록
	 */
	@Query("SELECT SUM(aa.amount) FROM AssetAccount aa " +
		"WHERE aa.user.userId = :userId " +
		"AND aa.isHidden = false " +
		"AND aa.accountType = :accountType " +
		"AND aa.isEnded = false")
	Long sumAmountByUserIdAndAccountType(
		@Param("userId") Long userId,
		@Param("accountType") AccountType accountType
	);
}