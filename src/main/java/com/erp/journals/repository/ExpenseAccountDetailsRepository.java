package com.erp.journals.repository;

import com.erp.journals.entity.ExpenseAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseAccountDetailsRepository extends JpaRepository<ExpenseAccountDetails, Integer> {

    @Query(value = "FROM ExpenseAccountDetails ead JOIN ead.expense e WHERE FUNCTION('YEAR', e.paymentDate) = :year AND FUNCTION('MONTH', e.paymentDate) = :month")
    List<ExpenseAccountDetails> findExpenseAccountDetailsByExpenseDate(int year, int month);

    @Query(value = "FROM ExpenseAccountDetails ead JOIN ead.expense e WHERE FUNCTION('YEAR', e.paymentDate) = :year AND FUNCTION('MONTH', e.paymentDate) = :month AND LOWER(e.paymentMode) = LOWER(:paymentMode)")
    List<ExpenseAccountDetails> findExpenseAccountDetailsByPaymentModeAndExpenseDate(int year, int month, String paymentMode);

    @Query(value = "FROM ExpenseAccountDetails ead JOIN ead.expense e JOIN ead.expenseTypes et WHERE et.type = :expenseType AND FUNCTION('YEAR', e.paymentDate) = :year AND FUNCTION('MONTH', e.paymentDate) = :month")
    List<ExpenseAccountDetails> findExpenseAccountDetailsByExpenseTypeAndExpenseDate(String expenseType, int year, int month);

}