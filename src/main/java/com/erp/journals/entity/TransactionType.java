package com.erp.journals.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_type")
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer transactionTypeId;

    @Column(name = "description")
    String description;

    public TransactionType(){}

    public TransactionType(Integer transactionTypeId, String description) {
        this.transactionTypeId = transactionTypeId;
        this.description = description;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
