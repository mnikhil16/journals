package com.erp.journals.repository;

import com.erp.journals.entity.ExpenseAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseAccountDetailsRepository extends JpaRepository<ExpenseAccountDetails, Integer> {
    @Query("FROM ExpenseAccountDetails ead " +
            "JOIN ead.expense exp " +
            "JOIN ead.expenseType et " +
            "WHERE FUNCTION('EXTRACT', YEAR FROM exp.paymentDate) = :year " +
            "AND FUNCTION('EXTRACT', MONTH FROM exp.paymentDate) = :month")
    List<ExpenseAccountDetails> findExpensesByExpenseDate(int year, int month);
}