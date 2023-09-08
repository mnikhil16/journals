package com.erp.journals.dto;

import java.sql.Date;

public class SalesInvoiceDTO {

    Integer salesInvoiceId;

    String customerName;

    Double amount;

    Date salesDate;

    String salesInvoiceNumber;

    Integer transaction_type_id;

    public SalesInvoiceDTO(){}

    public SalesInvoiceDTO(Integer salesInvoiceId, String customerName, Double amount, Date salesDate, String salesInvoiceNumber, Integer transaction_type_id) {
        this.salesInvoiceId = salesInvoiceId;
        this.customerName = customerName;
        this.amount = amount;
        this.salesDate = salesDate;
        this.salesInvoiceNumber = salesInvoiceNumber;
        this.transaction_type_id = transaction_type_id;
    }

    public Integer getSalesInvoiceId() {
        return salesInvoiceId;
    }

    public void setSalesInvoiceId(Integer salesInvoiceId) {
        this.salesInvoiceId = salesInvoiceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public String getSalesInvoiceNumber() {
        return salesInvoiceNumber;
    }

    public void setSalesInvoiceNumber(String salesInvoiceNumber) {
        this.salesInvoiceNumber = salesInvoiceNumber;
    }

    public Integer getTransaction_type_id() {
        return transaction_type_id;
    }

    public void setTransaction_type_id(Integer transaction_type_id) {
        this.transaction_type_id = transaction_type_id;
    }
}
