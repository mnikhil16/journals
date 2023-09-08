package com.erp.journals.repository;

import com.erp.journals.dto.SalesInvoiceDTO;
import com.erp.journals.entity.JournalEntry;
import com.erp.journals.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice,Integer> {
    List<SalesInvoice> findSalesInvoiceBySalesDate(LocalDate salesDate);
}
