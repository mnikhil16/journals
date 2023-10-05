package com.erp.journals.repository;

import com.erp.journals.entity.ExpenseTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseTypesRepository extends JpaRepository<ExpenseTypes,Integer> {
}
