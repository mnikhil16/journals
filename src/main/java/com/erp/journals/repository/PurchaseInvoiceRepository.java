package com.erp.journals.repository;

import com.erp.journals.entity.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, Integer> {

    @Query(value = "FROM PurchaseInvoice WHERE EXTRACT(YEAR FROM purchaseDate) = :year AND EXTRACT(MONTH FROM purchaseDate) = :month")
    List<PurchaseInvoice> findPurchaseInvoiceByPurchaseDate(int year, int month);

    @Query(value = "SELECT * FROM purchases_invoice WHERE EXTRACT(YEAR FROM pur_invoice_date) = :year AND EXTRACT(MONTH FROM pur_invoice_date) = :month AND LOWER((CAST(payment_details AS jsonb)->'paymentData'->'paymentList'->0->>'method')) = LOWER(:method)", nativeQuery = true)
    List<PurchaseInvoice> findPurchaseInvoiceByPaymentMethodAndPurchaseDate(int year, int month, String method);

}
