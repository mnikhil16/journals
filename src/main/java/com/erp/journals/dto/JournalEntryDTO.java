package com.erp.journals.dto;

import com.erp.journals.entity.SalesInvoice;
import com.erp.journals.entity.TransactionType;

import java.util.Date;

public class JournalEntryDTO {

    Integer journalId;

    Date journalDate;

    String description;

    String particulars;

    Character entryType;

    Double amount;

    String accountType;

    String transactionId;

    Integer transactionTypeId;

    SalesInvoiceDTO salesInvoiceDTO;

    public JournalEntryDTO(){}

    public JournalEntryDTO(Integer journalId, Date journalDate, String description, String particulars, Character entryType, Double amount, String accountType, String transactionId, Integer transactionTypeId, SalesInvoiceDTO salesInvoiceDTO) {
        this.journalId = journalId;
        this.journalDate = journalDate;
        this.description = description;
        this.particulars = particulars;
        this.entryType = entryType;
        this.amount = amount;
        this.accountType = accountType;
        this.transactionId = transactionId;
        this.transactionTypeId = transactionTypeId;
        this.salesInvoiceDTO = salesInvoiceDTO;
    }

    public Integer getJournalId() {
        return journalId;
    }

    public void setJournalId(Integer journalId) {
        this.journalId = journalId;
    }

    public Date getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(Date journalDate) {
        this.journalDate = journalDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public Character getEntryType() {
        return entryType;
    }

    public void setEntryType(Character entryType) {
        this.entryType = entryType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public SalesInvoiceDTO getSalesInvoiceDTO() {
        return salesInvoiceDTO;
    }

    public void setSalesInvoiceDTO(SalesInvoiceDTO salesInvoiceDTO) {
        this.salesInvoiceDTO = salesInvoiceDTO;
    }
}