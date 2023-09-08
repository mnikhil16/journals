package com.erp.journals.service;
import com.erp.journals.dto.JournalEntryDTO;
import com.erp.journals.dto.SalesInvoiceDTO;
import com.erp.journals.entity.SalesInvoice;
import com.erp.journals.mapper.SalesInvoiceMapper;
import com.erp.journals.repository.SalesInvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class CreateJournalEntry {

    Logger logger = LoggerFactory.getLogger(CreateJournalEntry.class);

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    SalesInvoiceRepository salesInvoiceRepository;

    @Scheduled(cron = "0 19 19 * * ?")
    public void createJournalEntriesForToday() {
        LocalDate today = LocalDate.now();
        logger.trace(today.toString());
        List<SalesInvoice> salesInvoices = salesInvoiceRepository.findSalesInvoiceBySalesDate(today);
        List<SalesInvoiceDTO> salesInvoiceDTOs = SalesInvoiceMapper.instance.modelToDtoList(salesInvoices);
        for (SalesInvoiceDTO salesInvoiceDTO : salesInvoiceDTOs) {
            List<JournalEntryDTO> journalEntries = journalEntryService.createJournalEntry(salesInvoiceDTO);
            logger.trace("Executing the create journalEntry for sales invoice with ID: " + salesInvoiceDTO.getSalesInvoiceId());
        }
    }

//    @Scheduled(cron = "0 0 0 * * ?")
    public void createAllJournalEntries() {
        List<SalesInvoice> salesInvoices = salesInvoiceRepository.findAll();
        List<SalesInvoiceDTO> salesInvoiceDTOs = SalesInvoiceMapper.instance.modelToDtoList(salesInvoices);
        for (SalesInvoiceDTO salesInvoiceDTO : salesInvoiceDTOs) {
            List<JournalEntryDTO> journalEntries = journalEntryService.createJournalEntry(salesInvoiceDTO);
            logger.trace("Executing the create journalEntry for sales invoice with ID: " + salesInvoiceDTO.getSalesInvoiceId());
        }
    }
}
