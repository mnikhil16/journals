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

    @Query(value = "FROM PurchaseInvoice WHERE paymentDetails IS NOT NULL AND FUNCTION('JSON_EXTRACT', paymentDetails, '$.paymentData.paymentList[0].method') = :method AND EXTRACT(YEAR FROM purchaseDate) = :year AND EXTRACT(MONTH FROM purchaseDate) = :month")
    List<PurchaseInvoice> findPurchaseInvoiceByPaymentMethodAndPurchaseDate(String method, int year, int month);

}
