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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class CreateLedgerService {
    Logger logger = LoggerFactory.getLogger(CreateLedgerService.class);

    @Autowired
    SaleRepository saleRepository;

    @Scheduled(cron ="30 0 18 * * ?")
    public void executeAndSaveLedgers() {

        Date startDate = Date.valueOf("2022-04-01");
        Date endDate = Date.valueOf("2022-04-30");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!startDate.after(endDate)) {
            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because months are 0-based
            int year = calendar.get(Calendar.YEAR);
            Month monthNumber = Month.of(month);
            String monthName = monthNumber.toString();

            logger.info("Scheduled task executing..." + startDate + ":" + endDate + monthName + "-" + year);


            // Fetch data for the current month and year
            List<Sale> saleList = saleRepository.findSalesByInvoiceDate(year, month);
            logger.info("Found {} sales invoices for {}_{}", saleList.size(), monthName, year);
            if(!saleList.isEmpty()) {
                createExcelForLedgers(saleList, "Ledgers.xlsx");
            }
            createPdfsForLedgers(saleList);

            calendar.add(Calendar.MONTH, 1);
            startDate = new Date(calendar.getTimeInMillis());
        }
    }
    public void createExcelForLedgers(List<Sale> saleList, String existingFilePath) {
        String sheetName = "Sale Ledger";

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
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                // Find the last row index
                int lastRow = sheet.getLastRowNum();

                // Create a merged header row
                createCategoryHeaderRow(sheet, lastRow + 1, "Sales");
            }

            // Find the last row index
            int lastRow = sheet.getLastRowNum();

            // Create header row (if it doesn't exist)
            if (sheet.getLastRowNum() < lastRow + 2) {
                createHeaderRow(sheet, lastRow + 3);
            }

            int rowNum = lastRow + 3; // Start row index

            for (Sale sale : saleList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(sale.getInvoiceDate().format(formatter));
                row1.createCell(1).setCellValue(sale.extractPaymentMethod() == null ? "by cash" : "by "+ sale.extractPaymentMethod().toLowerCase());
                row1.createCell(2).setCellValue("");
                row1.createCell(3).setCellValue(sale.getPaidAmount());
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated for Sale: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForLedgers(List<Sale> saleList) {
        String pdfFileName = "sale_ledger" + ".pdf";

        try {
            Document document = new Document();

            // Check if the PDF file already exists
            if (new File(pdfFileName).exists()) {
                PdfWriter.getInstance(document, new FileOutputStream(pdfFileName, true)); // Append mode
            }
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));


            document.open();

            PdfPTable table1 = new PdfPTable(4);
            Stream.of("Date", "Particulars", "Debit Amount", "Credit Amount")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table1.addCell(header);
                    });
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

            for (Sale sale : saleList) {
                table1.addCell(sale.getInvoiceDate().format(formatter));
                table1.addCell(sale.extractPaymentMethod() == null ? "by cash" : "by "+ sale.extractPaymentMethod().toLowerCase());
                table1.addCell("");
                table1.addCell(sale.getPaidAmount().toString());
            }

//Adding new page after every table
            document.add(table1);
            document.newPage();
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCategoryHeaderRow(Sheet sheet, int rowIndex, String category) {
        Row headerRow = sheet.createRow(rowIndex);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue(category);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 6));
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    private void createHeaderRow(Sheet sheet, int rowIndex) {
        Row headerRow = sheet.createRow(rowIndex - 1);

        // Create a cell style with blue background and centered alignment
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        // Create a font with white color
        Font font = sheet.getWorkbook().createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        // Create cells with the specified style for each column
        for (int i = 0; i < 7; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(getHeaderTitle(i));
            cell.setCellStyle(style);
        }
    }
    private String getHeaderTitle(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Date";
            case 1:
                return "Particulars";
            case 2:
                return "Debit Amount";
            case 3:
                return "Credit Amount";
            default:
                return "";
        }
    }
}