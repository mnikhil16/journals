package com.erp.journals.repository;

import com.erp.journals.entity.Receivables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivables, Integer> {

    @Query(value = "FROM Receivables WHERE EXTRACT(YEAR FROM paymentDate) = :year AND EXTRACT(MONTH FROM paymentDate) = :month")
    List<Receivables> findReceivablesByPaymentDate(int year, int month);

    @Query(value = "FROM Receivables WHERE EXTRACT(YEAR FROM paymentDate) = :year AND EXTRACT(MONTH FROM paymentDate) = :month AND LOWER(paymentMode) = LOWER(:paymentMode)" )
    List<Receivables> findReceivablesByPaymentModeAndPaymentDate(int year, int month, String paymentMode);

}
