package com.erp.journals.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "expense")
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "payment_mode")
    private String paymentMode;
}
