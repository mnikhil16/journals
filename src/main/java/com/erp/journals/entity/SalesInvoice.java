package com.erp.journals.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "sales_invoice")
public class SalesInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer salesInvoiceId;

    @Column(name = "customer_name")
    String customerName;

    @Column(name = "amount")
    Double amount;

    @Column(name = "sales_date")
    Date salesDate;

    @Column(name = "sales_invoice_no")
    String salesInvoiceNumber;

    public SalesInvoice(){}

    public SalesInvoice(Integer salesInvoiceId, String customerName, Double amount, Date salesDate, String salesInvoiceNumber) {
        this.salesInvoiceId = salesInvoiceId;
        this.customerName = customerName;
        this.amount = amount;
        this.salesDate = salesDate;
        this.salesInvoiceNumber = salesInvoiceNumber;
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
}
