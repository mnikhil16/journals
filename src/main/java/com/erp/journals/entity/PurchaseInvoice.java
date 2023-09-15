package com.erp.journals.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "purchases_invoice")
public class PurchaseInvoice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "purchaseamount")
    private Integer purchaseAmount;

    @Column(name = "pur_invoice_date")
    private ZonedDateTime purchaseDate;

    @Column(name = "payment_details")
    private String paymentDetails;
}