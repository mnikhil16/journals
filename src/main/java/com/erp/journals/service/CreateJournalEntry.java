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

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CreateJournalEntry {

    Logger logger = LoggerFactory.getLogger(CreateJournalEntry.class);

    @Autowired
    SaleRepository saleRepository;

    @Scheduled(cron = "0 43 19 * * ?")
    public void executeAndSaveJournalEntries() {
        LocalDate startDate = LocalDate.of(2020, 12, 19);
        LocalDate endDate = LocalDate.of(2023, 9, 10);

        while (!startDate.isAfter(endDate)) {
            Year year = Year.of(startDate.getYear());
            Month month = startDate.getMonth();

            // Fetch sales data for the current month and year
            List<Sale> sales = saleRepository.findSaleBySaleMonthAndYear(month, year);

            if (!sales.isEmpty()) {
                createJournalEntriesForSale(month, year, sales);
            }

            // Move to the next month
            startDate = startDate.plusMonths(1);
        }
    }

    public void createJournalEntriesForSale(Month month, Year year, List<Sale> sales) {
        String monthYearString = month.toString().toLowerCase() + "_" + year.toString();

        // Create an Excel workbook and sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            String excelFileName = "journal_entries";
            Sheet sheet = workbook.createSheet(monthYearString);

            int rowNum = 0;

            // Create header row
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Date");
            headerRow.createCell(1).setCellValue("Description");
            headerRow.createCell(2).setCellValue("Particulars");
            headerRow.createCell(3).setCellValue("Entry Type");
            headerRow.createCell(4).setCellValue("Amount");
            headerRow.createCell(5).setCellValue("Account Type");

            // Create a PDF
            Document document = new Document();
            String pdfFileName = "journal_entries_" + monthYearString + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));

            document.open();

            PdfPTable table = new PdfPTable(6);
            Stream.of("Date","Description","Particulars","Entry Type","Amount","Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            for (Sale sale : sales) {
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(sale.getInvoiceDate());
                row1.createCell(1).setCellValue("sold goods to " + sale.getCustomer().getDisplayName() + " for rs " + sale.getPaidAmount());
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(sale.getPaidAmount().toString());
                row1.createCell(5).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue(sale.getInvoiceDate());
                row2.createCell(1).setCellValue("sold goods to " + sale.getCustomer().getDisplayName() + " for rs " + sale.getPaidAmount());
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(sale.getPaidAmount().toString());
                row2.createCell(5).setCellValue("nominal account");

                table.addCell(sale.getInvoiceDate().toString());
                table.addCell("sold goods to " + sale.getCustomer().getDisplayName() + " for rs " + sale.getPaidAmount());
                table.addCell("cash a/c");
                table.addCell("d");
                table.addCell(sale.getPaidAmount().toString());
                table.addCell("real account");

                table.addCell(sale.getInvoiceDate().toString());
                table.addCell("sold goods to " + sale.getCustomer().getDisplayName() + " for rs " + sale.getPaidAmount());
                table.addCell("to sales a/c");
                table.addCell("c");
                table.addCell(sale.getPaidAmount().toString());
                table.addCell("nominal account");
            }

            // Save the Excel file
            try (FileOutputStream outputStream = new FileOutputStream(excelFileName)) {
                workbook.write(outputStream);
            }

            // Save the PDF file
            document.add(table);
            document.close();

            logger.trace("Journal entries exported to Excel and PDF for " + monthYearString + ".");
        } catch (IOException | DocumentException e) {
            logger.error("Error while creating files for " + monthYearString + ": " + e.getMessage());
        }
    }
}