package com.erp.journals.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "journal_entry")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer journalId;

    @JoinColumn(name = "journal_date")
    Date journalDate;

    @JoinColumn(name = "description")
    String description;

    @JoinColumn(name = "particulars")
    String particulars;

    @JoinColumn(name = "entry_type")
    Character entryType;

    @JoinColumn(name = "amount")
    Double amount;

    @JoinColumn(name = "account_type")
    String accountType;

    @JoinColumn(name = "transaction_id")
    String transactionId;

    @JoinColumn(name = "transaction_type_id")
    Integer transactionTypeId;

    @ManyToOne
    @JoinColumn(name = "sales_invoice_id")
    SalesInvoice salesInvoice;

    public JournalEntry(){}

    public JournalEntry(Integer journalId, Date journalDate, String description, String particulars, Character entryType, Double amount, String accountType, String transactionId, Integer transactionTypeId, SalesInvoice salesInvoice) {
        this.journalId = journalId;
        this.journalDate = journalDate;
        this.description = description;
        this.particulars = particulars;
        this.entryType = entryType;
        this.amount = amount;
        this.accountType = accountType;
        this.transactionId = transactionId;
        this.transactionTypeId = transactionTypeId;
        this.salesInvoice = salesInvoice;
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

    public SalesInvoice getSalesInvoice() {
        return salesInvoice;
    }

    public void setSalesInvoice(SalesInvoice salesInvoice) {
        this.salesInvoice = salesInvoice;
    }
}
