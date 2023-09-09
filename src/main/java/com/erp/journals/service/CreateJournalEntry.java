package com.erp.journals.service;
import com.erp.journals.dto.SalesInvoiceDTO;
import com.erp.journals.entity.SalesInvoice;
import com.erp.journals.mapper.SalesInvoiceMapper;
import com.erp.journals.repository.SalesInvoiceRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class CreateJournalEntry {

    Logger logger = LoggerFactory.getLogger(CreateJournalEntry.class);

    @Autowired
    SalesInvoiceRepository salesInvoiceRepository;

    @Scheduled(cron = "0 59 23 * * ?")
    public void createJournalEntriesForToday() {
        Date today = new Date();
        List<SalesInvoice> salesInvoices = salesInvoiceRepository.findSalesInvoiceBySalesDate(today);
        List<SalesInvoiceDTO> salesInvoiceDTOs = SalesInvoiceMapper.instance.modelToDtoList(salesInvoices);

        // Create an Excel workbook and sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("JournalEntries");

            int rowNum = 0;

            // Create header row
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Date");
            headerRow.createCell(1).setCellValue("Description");
            headerRow.createCell(2).setCellValue("Particulars");
            headerRow.createCell(3).setCellValue("Entry Type");
            headerRow.createCell(4).setCellValue("Amount");
            headerRow.createCell(5).setCellValue("Account Type");

            for (SalesInvoiceDTO salesInvoiceDTO : salesInvoiceDTOs) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(salesInvoiceDTO.getSalesDate().toString());
                row1.createCell(1).setCellValue("sold goods to "+salesInvoiceDTO.getCustomerName()+" for rs "+salesInvoiceDTO.getAmount());
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(salesInvoiceDTO.getAmount());
                row1.createCell(5).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue(salesInvoiceDTO.getSalesDate().toString());
                row2.createCell(1).setCellValue("sold goods to "+salesInvoiceDTO.getCustomerName()+" for rs "+salesInvoiceDTO.getAmount());
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(salesInvoiceDTO.getAmount());
                row2.createCell(5).setCellValue("nominal account");
            }
            // Save the Excel file
            try (FileOutputStream outputStream = new FileOutputStream("journal_entries.xlsx")) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            logger.error("Error while creating Excel file: " + e.getMessage());
        }

        logger.trace("Journal entries exported to Excel for today's sales invoices.");
    }
}