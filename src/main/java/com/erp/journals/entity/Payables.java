package com.erp.journals.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "payables")
public class Payables implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Suppliers supplier;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "amount")
    private Integer amount;
}
