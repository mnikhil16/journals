package com.erp.journals.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "payment")
public class Receivables implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "payment_mode")
    private String paymentMode;

    // bidirectional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
}
