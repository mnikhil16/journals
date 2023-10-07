package com.erp.journals.repository;

import com.erp.journals.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Integer> {

    @Query(value = "Select s FROM Sale s WHERE EXTRACT(YEAR FROM invoiceDate) = :year AND EXTRACT(MONTH FROM invoiceDate) = :month")
    List<Sale> findSalesByInvoiceDate(int year, int month);

    @Query(value = "SELECT * FROM sales WHERE EXTRACT(YEAR FROM invoice_date) = :year AND EXTRACT(MONTH FROM invoice_date) = :month AND LOWER((ext_data->'paymentData'->'paymentList'->0->>'method')) = LOWER(:method)", nativeQuery = true)
    List<Sale> findSalesByInvoiceDateAndPaymentMethod(int year, int month, String method);
}