package com.erp.journals.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class LedgerDTO {
    private ZonedDateTime date;
    private String particulars;
    private Integer debitAmount;
    private Integer creditAmount;
}