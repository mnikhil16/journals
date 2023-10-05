package com.erp.journals.repository;

import com.erp.journals.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Integer> {

    @Query(value = "FROM Sale WHERE EXTRACT(YEAR FROM invoiceDate) = :year AND EXTRACT(MONTH FROM invoiceDate) = :month")
    List<Sale> findSalesByInvoiceDate(int year, int month);

    @Query(value = "FROM Sale WHERE extData IS NOT NULL AND FUNCTION('JSON_EXTRACT', extData, '$.paymentData.paymentList[0].method') = :method AND EXTRACT(YEAR FROM invoiceDate) = :year AND EXTRACT(MONTH FROM invoiceDate) = :month")
    List<Sale> findSalesByPaymentMethodAndInvoiceDate(String method, int month, int year);

}