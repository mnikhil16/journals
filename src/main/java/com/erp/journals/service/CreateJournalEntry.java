package com.erp.journals.service;


import com.erp.journals.entity.Sale;
import com.erp.journals.repository.SaleRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CreateJournalEntry {

    Logger logger = LoggerFactory.getLogger(CreateJournalEntry.class);

    @Autowired
    SaleRepository saleRepository;

    @Scheduled(cron ="30 22 21 * * ?")
    public void executeAndSaveJournalEntries() throws IOException {

        Date startDate = Date.valueOf("2022-04-01");
        Date endDate = Date.valueOf("2022-05-31");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!startDate.after(endDate)) {
            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because months are 0-based
            int year = calendar.get(Calendar.YEAR);
            Month monthNumber = Month.of(month);
            String monthName = monthNumber.toString();

            logger.info("Scheduled task executing..." + startDate + ":" + endDate + monthName + "-" + year);


            // Fetch sales data for the current month and year
            List<Sale> saleList = saleRepository.findSalesByInvoiceDate(year, month);
            logger.info("Found {} sales invoices for {}_{}", saleList.size(), monthName, year);


            if (!saleList.isEmpty()) {
                createExcelForJournalEntries(month, year, saleList, "JournalEntries.xlsx");
                createPdfsForJournalEntries(month, year, saleList);
            }

            // Move to the next month
            calendar.add(Calendar.MONTH, 1);
            startDate = new Date(calendar.getTimeInMillis());
        }
    }

    public void createExcelForJournalEntries(int month, int year, List<Sale> saleList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + "_" + year;

        try {
            // Load the existing Excel workbook if it exists, or create a new one if it doesn't
            Workbook workbook;
            if (new File(existingFilePath).exists()) {
                FileInputStream inputStream = new FileInputStream(existingFilePath);
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new XSSFWorkbook();
            }

            // Create or get the sheet with the given name
            Sheet sheet = workbook.getSheet(monthYearString);
            if (sheet == null) {
                sheet = workbook.createSheet(monthYearString);
                int rowNum = 0;

                // Create header row (if it doesn't exist)
                if (sheet.getLastRowNum() == -1) {
                    Row headerRow = sheet.createRow(rowNum++);
                    headerRow.createCell(0).setCellValue("Date");
                    headerRow.createCell(1).setCellValue("Description");
                    headerRow.createCell(2).setCellValue("Particulars");
                    headerRow.createCell(3).setCellValue("Entry Type");
                    headerRow.createCell(4).setCellValue("Amount");
                    headerRow.createCell(5).setCellValue("Account Type");
                }
            }

            int rowNum = sheet.getLastRowNum() + 1;

            for (Sale sale : saleList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(sale.getInvoiceDate().format(formatter));
                row1.createCell(1).setCellValue("sold goods to " + (sale.getCustomer() == null  ?"" : sale.getCustomer().getFirstName() + " for rs " + sale.getPaidAmount()));
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(sale.getPaidAmount());
                row1.createCell(5).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(sale.getPaidAmount());
                row2.createCell(5).setCellValue("nominal account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntries(int month, int year, List<Sale> saleList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_" + monthName + "_" + year + ".pdf";

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            PdfPTable table = new PdfPTable(6);
            Stream.of("Date", "Description", "Particulars", "Entry Type", "Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            for (Sale sale : saleList) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                table.addCell(sale.getInvoiceDate().format(formatter));
                table.addCell("sold goods to " + (sale.getCustomer() == null  ?"" : sale.getCustomer().getFirstName() + " for rs " + sale.getPaidAmount()));
                table.addCell("cash a/c");
                table.addCell("d");
                table.addCell(sale.getPaidAmount().toString());
                table.addCell("real account");

                table.addCell("");
                table.addCell("");
                table.addCell("to sales a/c");
                table.addCell("c");
                table.addCell(sale.getPaidAmount().toString());
                table.addCell("nominal account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}