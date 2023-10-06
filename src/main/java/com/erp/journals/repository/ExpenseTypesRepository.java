package com.erp.journals.repository;

import com.erp.journals.entity.ExpenseTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseTypesRepository extends JpaRepository<ExpenseTypes,Integer> {

    @Query(value = "SELECT DISTINCT et.type FROM ExpenseTypes et")
    List<String> findAllExpenseTypes();

}
