package com.erp.journals.entity;

//import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
//import com.vladmihalcea.hibernate.type.json.JsonStringType;
//import org.hibernate.annotations.Type;
//import org.hibernate.annotations.TypeDef;
//import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/** The persistent class for the sales database table. */
@Data
@Entity
@Table(name = "sales")
@NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s")
//@TypeDefs({
//        @TypeDef(name = "json", typeClass = JsonStringType.class),
//        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})
public class Sale  {
//    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "paid_amt")
    private BigDecimal paidAmount;

    // bi-directional many-to-one association to Customer
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Customer customer;

}

