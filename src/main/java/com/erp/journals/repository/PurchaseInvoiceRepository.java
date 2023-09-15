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
}
