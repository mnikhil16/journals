package com.erp.journals.repository;

import com.erp.journals.entity.Payables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayablesRepository extends JpaRepository<Payables, Integer> {

    @Query(value = "FROM Payables WHERE EXTRACT(YEAR FROM paymentDate) = :year AND EXTRACT(MONTH FROM paymentDate) = :month")
    List<Payables> findPayablesByPaymentDate(int year, int month);

    @Query(value = "FROM Payables WHERE paymentMode = :paymentMode AND EXTRACT(YEAR FROM paymentDate) = :year AND EXTRACT(MONTH FROM paymentDate) = :month")
    List<Payables> findPayablesByPaymentModeAndPaymentDate(String paymentMode, int year, int month);

}
