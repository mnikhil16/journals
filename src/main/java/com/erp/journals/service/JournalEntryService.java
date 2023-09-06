package com.erp.journals.service;

import com.erp.journals.dto.JournalEntryDTO;
import com.erp.journals.dto.SalesInvoiceDTO;
import com.erp.journals.entity.JournalEntry;
import com.erp.journals.entity.SalesInvoice;
import com.erp.journals.entity.TransactionType;
import com.erp.journals.mapper.JournalEntryMapper;
import com.erp.journals.mapper.SalesInvoiceMapper;
import com.erp.journals.repository.JournalEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class JournalEntryService {
    @Autowired
    JournalEntryRepository journalEntryRepository;

    Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    public List<JournalEntryDTO> getJournalEntries(){
        logger.trace("Get all Journal Entries is invoked");
        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        List<JournalEntryDTO> journalEntryDTOList = JournalEntryMapper.instance.modelToDtoList(journalEntryList);
        return journalEntryDTOList;
    }

    public JournalEntryDTO getJournalEntryById(int journalEntryId){
        logger.trace("Get Journal Entry By Id service is invoked.");
        JournalEntry journalEntry = journalEntryRepository.findById(journalEntryId).get();
        JournalEntryDTO journalEntryDTO = JournalEntryMapper.instance.modelToDto(journalEntry);
        return journalEntryDTO;
    }

    public List<JournalEntryDTO> createJournalEntry(SalesInvoiceDTO salesInvoiceDto){
        SalesInvoice salesInvoice = SalesInvoiceMapper.instance.dtoToModel(salesInvoiceDto);
        List<JournalEntry> journalEntryList = new ArrayList<>();
        JournalEntry journalEntry1 = new JournalEntry();
        journalEntry1.setJournalDate(salesInvoice.getSalesDate());
        journalEntry1.setDescription("sold goods to "+salesInvoice.getCustomerName()+" for rs "+salesInvoice.getAmount());
        journalEntry1.setParticulars("cash a/c");
        journalEntry1.setEntryType('d');
        journalEntry1.setAmount(salesInvoice.getAmount());
        journalEntry1.setAccountType("real account");
        journalEntryList.add(journalEntry1);
        journalEntryRepository.save(journalEntry1);

        JournalEntry journalEntry2 = new JournalEntry();
        journalEntry2.setJournalDate(salesInvoice.getSalesDate());
        journalEntry2.setDescription("sold goods to "+salesInvoice.getCustomerName()+" for rs "+salesInvoice.getAmount());
        journalEntry2.setParticulars("to sales a/c");
        journalEntry2.setEntryType('c');
        journalEntry2.setAmount(salesInvoice.getAmount());
        journalEntry2.setAccountType("nominal account");
        journalEntryList.add(journalEntry2);
        journalEntryRepository.save(journalEntry2);

        List<JournalEntryDTO> journalEntryDTOList = JournalEntryMapper.instance.modelToDtoList(journalEntryList);

        return journalEntryDTOList;
    }

}
