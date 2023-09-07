package com.erp.journals.repository;

import com.erp.journals.entity.JournalEntry;
import com.erp.journals.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Integer> {
    List<JournalEntry> findBySalesInvoiceSalesInvoiceId(Integer salesInvoiceId);
}
