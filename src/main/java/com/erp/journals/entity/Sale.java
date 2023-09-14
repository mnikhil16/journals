package com.erp.journals.entity;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.time.ZonedDateTime;

/** The persistent class for the sales database table. */
@Data
@Entity
@Table(name = "sales")
@NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s")
public class Sale  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Column(name = "invoice_date")
    private ZonedDateTime invoiceDate;

    @Column(name = "paid_amt")
    private Integer paidAmount;

    // bidirectional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

}

