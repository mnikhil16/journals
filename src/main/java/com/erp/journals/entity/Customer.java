package com.erp.journals.entity;

import javax.persistence.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/** The persistent class for the customers database table. */
@Data
@Entity
@Table(name = "customers")
@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

}