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

	@Query("SELECT AVG(e.amount) " +
		"FROM Expense e " +
		"JOIN e.couple c " +
		"JOIN User u1 ON c.user1.userId = u1.userId " +
		"JOIN User u2 ON c.user2.userId = u2.userId " +
		"WHERE ((u1.age + u2.age) / 2) BETWEEN :startAge AND :endAge " +
		"AND (u1.monthlyIncome + u2.monthlyIncome) BETWEEN :startIncome AND :endIncome " +
		"AND e.date >= CURRENT_DATE - 1 MONTH")
	Double findAverageExpenditureForEligibleCouples(@Param("startAge") int startAge, @Param("endAge") int endAge,
		@Param("startIncome") long startIncome, @Param("endIncome") long endIncome);

	@Query("SELECT SUM(e.amount) " +
		"FROM Expense e " +
		"WHERE e.couple.coupleId = :coupleId " +
		"AND e.date >= CURRENT_DATE - 1 MONTH")
	Long findTotalExpenditureForCoupleLastMonth(@Param("coupleId") Long coupleId);

	@Query("SELECT e.category " +
		"FROM Expense e " +
		"WHERE e.couple.coupleId = :coupleId " +
		"AND e.date >= CURRENT_DATE - 1 MONTH " +
		"GROUP BY e.category " +
		"ORDER BY SUM(e.amount) DESC")
	String findTopCategoryForCoupleLastMonth(@Param("coupleId") Long coupleId);

}
