package com.erp.journals.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "expense_account_details")
public class ExpenseAccountDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expense_type")
    private ExpenseTypes expenseTypes;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Integer amount;

    // bidirectional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.EAGER)
    private Expense expense;
}
