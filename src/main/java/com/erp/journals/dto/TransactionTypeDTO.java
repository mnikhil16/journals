package com.erp.journals.dto;

public class TransactionTypeDTO {

    Integer transactionTypeId;

    String description;

    public TransactionTypeDTO(){}

    public TransactionTypeDTO(Integer transactionTypeId, String description) {
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
