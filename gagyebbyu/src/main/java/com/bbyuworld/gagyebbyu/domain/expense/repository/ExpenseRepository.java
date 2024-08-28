package com.bbyuworld.gagyebbyu.domain.expense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.expense.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, ExpenseCustomRepository {

	@Modifying
	@Query("update Expense e set e.couple = :couple where e.user.userId = :user1Id or e.user.userId = :user2Id")
	int updateExpenseCouple(@Param("couple") Couple couple, @Param("user1Id") Long user1Id,
		@Param("user2Id") Long user2Id);

	@Query("SELECT SUM(e.amount) " +
		"FROM Expense e " +
		"WHERE e.couple.coupleId = :coupleId " +
		"AND e.date >= CURRENT_DATE - 1 MONTH")
	Long findTotalExpenditureForCoupleLastMonth(@Param("coupleId") Long coupleId);

}
