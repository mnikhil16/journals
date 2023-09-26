package com.erp.journals.service;

import com.erp.journals.entity.*;
import com.erp.journals.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
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
public class CreateJournalEntryService {

    Logger logger = LoggerFactory.getLogger(CreateJournalEntryService.class);

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    ReceivableRepository receivableRepository;

    @Autowired
    ExpenseAccountDetailsRepository expenseAccountDetailsRepository;

    @Autowired
    PayablesRepository payablesRepository;

    @Autowired
    PurchaseInvoiceRepository purchaseInvoiceRepository;

    @Scheduled(cron ="20 21 23 * * ?")
    public void executeAndSaveJournalEntries() {

        Date startDate = Date.valueOf("2022-04-01");
        Date endDate = Date.valueOf("2023-03-31");

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
            List<Receivables> receivablesList = receivableRepository.findReceivablesByInvoiceDate(year, month);
            List<ExpenseAccountDetails> expenseAccountDetailsList = expenseAccountDetailsRepository.findExpenseAccountDetailsByExpenseDate(year, month);
            List<Payables> payablesList = payablesRepository.findPayablesByPaymentDate(year, month);
            List<PurchaseInvoice> purchaseInvoiceList = purchaseInvoiceRepository.findPurchaseInvoiceByPurchaseDate(year, month);

            logger.info("Found {} sales invoices for {}_{}", saleList.size(), monthName, year);
            logger.info("Found {} receivables for {}_{}", receivablesList.size(), monthName, year);
            logger.info("Found {} expenses for {}_{}", expenseAccountDetailsList.size(), monthName, year);
            logger.info("Found {} payables for {}_{}", payablesList.size(), monthName, year);
            logger.info("Found {} purchases for {}_{}", purchaseInvoiceList.size(), monthName, year);



//            if(!saleList.isEmpty()) {
//                createExcelForJournalEntriesSale(month, year, saleList, "JournalEntries.xlsx");
//            }
//
//            if(!receivablesList.isEmpty()) {
//                createExcelForJournalEntriesReceivable(month, year, receivablesList, "JournalEntries.xlsx");
//            }
//
//            if(!expenseAccountDetailsList.isEmpty()) {
//                createExcelForJournalEntriesExpense(month, year, expenseAccountDetailsList, "JournalEntries.xlsx");
//            }
//
//            if(!payablesList.isEmpty()) {
//                createExcelForJournalEntriesPayable(month, year, payablesList, "JournalEntries.xlsx");
//            }
//
//            if(!purchaseInvoiceList.isEmpty()) {
//                createExcelForJournalEntriesPurchase(month, year, purchaseInvoiceList, "JournalEntries.xlsx");
//            }

            createPdfsForJournalEntries(month, year, saleList, receivablesList, expenseAccountDetailsList, payablesList, purchaseInvoiceList);

            calendar.add(Calendar.MONTH, 1);
            startDate = new Date(calendar.getTimeInMillis());
        }
    }

    public void createExcelForJournalEntriesSale(int month, int year, List<Sale> saleList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + " " + year;

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
            }

            // Find the last row index
            int lastRow = sheet.getLastRowNum();

            // Create a merged header row
            createCategoryHeaderRow(sheet, lastRow + 1, "Sales");

            // Create header row (if it doesn't exist)
            if (sheet.getLastRowNum() < lastRow + 2) {
                createHeaderRow(sheet, lastRow + 3);
            }

            int rowNum = lastRow + 3; // Start row index

            for (Sale sale : saleList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(sale.getInvoiceDate().format(formatter));
                row1.createCell(1).setCellValue("sold goods to " + (sale.getCustomer() == null  ?"" : sale.getCustomer().getFirstName() + " for rs " + sale.getPaidAmount()));
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(sale.getPaidAmount());
                row1.createCell(6).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(5).setCellValue(sale.getPaidAmount());
                row2.createCell(6).setCellValue("nominal account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated for Sale: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createExcelForJournalEntriesReceivable(int month, int year, List<Receivables> receivablesList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + " " + year;

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
            }

            // Find the last row index
            int lastRow = sheet.getLastRowNum();

            sheet.createRow(lastRow);

            // Create a merged header row
            createCategoryHeaderRow(sheet, lastRow + 1, "Receivables");

            // Create header row (if it doesn't exist)
            if (sheet.getLastRowNum() < lastRow + 2) {
                createHeaderRow(sheet, lastRow + 3);
            }

            int rowNum = lastRow + 3; // Start row

            for (Receivables receivables: receivablesList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(receivables.getPaymentDate().format(formatter));
                row1.createCell(1).setCellValue("sold goods to " + (receivables.getCustomer() == null  ?"" : receivables.getCustomer().getFirstName() + " for rs " + receivables.getAmount()));
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(receivables.getAmount());
                row1.createCell(6).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(5).setCellValue(receivables.getAmount());
                row2.createCell(6).setCellValue("nominal account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated for Receivables: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createExcelForJournalEntriesExpense(int month, int year, List<ExpenseAccountDetails> expenseAccountDetailsList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + " " + year;

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
            }

            // Find the last row index
            int lastRow = sheet.getLastRowNum();

            sheet.createRow(lastRow);

            // Create a merged header row
            createCategoryHeaderRow(sheet, lastRow + 1, "Expenses");

            // Create header row (if it doesn't exist)
            if (sheet.getLastRowNum() < lastRow + 2) {
                createHeaderRow(sheet, lastRow + 3);
            }

            int rowNum = lastRow + 3; // Start row index

            for (ExpenseAccountDetails expenseAccountDetails: expenseAccountDetailsList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(expenseAccountDetails.getExpense().getPaymentDate().format(formatter));
                row1.createCell(1).setCellValue("Paid for "+(expenseAccountDetails.getExpenseTypes() == null  ?"" : expenseAccountDetails.getExpenseTypes().getType())+" to " + (expenseAccountDetails.getDescription() == null ? "":expenseAccountDetails.getDescription()) + " rs " + expenseAccountDetails.getAmount());
                row1.createCell(2).setCellValue(expenseAccountDetails.getExpenseTypes().getType()+" a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(expenseAccountDetails.getAmount());
                row1.createCell(6).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to "+expenseAccountDetails.getExpense().getPaymentMode()+" a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(5).setCellValue(expenseAccountDetails.getAmount());
                row2.createCell(6).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated for Expense: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createExcelForJournalEntriesPayable(int month, int year, List<Payables> payablesList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + " " + year;

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
            }

            // Find the last row index
            int lastRow = sheet.getLastRowNum();

            sheet.createRow(lastRow);

            // Create a merged header row
            createCategoryHeaderRow(sheet, lastRow + 1, "Payables");

            // Create header row (if it doesn't exist)
            if (sheet.getLastRowNum() < lastRow + 2) {
                createHeaderRow(sheet, lastRow + 3);
            }

            int rowNum = lastRow + 3; // Start row index

            for (Payables payables: payablesList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(payables.getPaymentDate().format(formatter));
                row1.createCell(1).setCellValue("purchased goods from " + (payables.getSupplier() == null  ?"" : payables.getSupplier().getFirstName()+" "+ payables.getSupplier().getLastName() + " worth rs " + payables.getAmount()));
                row1.createCell(2).setCellValue("purchase a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(payables.getAmount());
                row1.createCell(6).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to cash a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(5).setCellValue(payables.getAmount());
                row2.createCell(6).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated for Payables: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createExcelForJournalEntriesPurchase(int month, int year, List<PurchaseInvoice> purchaseInvoiceList, String existingFilePath) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String monthYearString = monthName + " " + year;

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
            }

            // Find the last row index
            int lastRow = sheet.getLastRowNum();

            sheet.createRow(lastRow);

            // Create a merged header row
            createCategoryHeaderRow(sheet, lastRow + 1, "Purchases");

            // Create header row (if it doesn't exist)
            if (sheet.getLastRowNum() < lastRow + 2) {
                createHeaderRow(sheet, lastRow + 3);
            }

            int rowNum = lastRow + 3; // Start row index

            for (PurchaseInvoice purchaseInvoice: purchaseInvoiceList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(purchaseInvoice.getPurchaseDate().format(formatter));
                row1.createCell(1).setCellValue("purchased stock items worth rs " + purchaseInvoice.getPurchaseAmount() + " and paid through cash");
                row1.createCell(2).setCellValue("purchase a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(purchaseInvoice.getPurchaseAmount());
                row1.createCell(6).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to cash a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(5).setCellValue(purchaseInvoice.getPurchaseAmount());
                row2.createCell(6).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated for Purchase: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntries(int month, int year, List<Sale> saleList, List<Receivables> receivablesList, List<ExpenseAccountDetails> expenseAccountDetailsList, List<Payables> payablesList, List<PurchaseInvoice> purchaseInvoiceList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_" + monthName + "_" + year + ".pdf";

        try {
            Document document = new Document();

            // Check if the PDF file already exists
            if (new File(pdfFileName).exists()) {
                PdfWriter.getInstance(document, new FileOutputStream(pdfFileName, true)); // Append mode
            }
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));


            document.open();

            PdfPTable table1 = new PdfPTable(7);
            Stream.of("Date", "Description", "Particulars", "Entry Type", "Debit Amount", "Credit Amount", "Account Type")
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
                table1.addCell("sold goods to " + (sale.getCustomer() == null  ?"" : sale.getCustomer().getFirstName() + " for rs " + sale.getPaidAmount()));
                table1.addCell("cash a/c");
                table1.addCell("d");
                table1.addCell(sale.getPaidAmount().toString());
                table1.addCell("");
                table1.addCell("real account");

                table1.addCell("");
                table1.addCell("");
                table1.addCell("to sales a/c");
                table1.addCell("c");
                table1.addCell("");
                table1.addCell(sale.getPaidAmount().toString());
                table1.addCell("nominal account");
            }

            PdfPTable table2 = new PdfPTable(7);

            Stream.of("Date", "Description", "Particulars", "Entry Type", "Debit Amount", "Credit Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table2.addCell(header);
                    });

            for (Receivables receivables: receivablesList) {
                table2.addCell(receivables.getPaymentDate().format(formatter));
                table2.addCell("sold goods to " + (receivables.getCustomer() == null  ?"" : receivables.getCustomer().getFirstName() + " for rs " + receivables.getAmount()));
                table2.addCell("cash a/c");
                table2.addCell("d");
                table2.addCell(receivables.getAmount().toString());
                table2.addCell("");
                table2.addCell("real account");

                table2.addCell("");
                table2.addCell("");
                table2.addCell("to sales a/c");
                table2.addCell("c");
                table2.addCell("");
                table2.addCell(receivables.getAmount().toString());
                table2.addCell("nominal account");
            }

            PdfPTable table3 = new PdfPTable(7);

            Stream.of("Date", "Description", "Particulars", "Entry Type", "Debit Amount", "Credit Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table3.addCell(header);
                    });

            for (ExpenseAccountDetails expenseAccountDetails: expenseAccountDetailsList) {
                table3.addCell(expenseAccountDetails.getExpense().getPaymentDate().format(formatter));
                table3.addCell("Paid for "+(expenseAccountDetails.getExpenseTypes() == null  ?"" : expenseAccountDetails.getExpenseTypes().getType())+" to " + (expenseAccountDetails.getDescription() == null ? "":expenseAccountDetails.getDescription()) + " rs " + expenseAccountDetails.getAmount());
                table3.addCell(expenseAccountDetails.getExpenseTypes().getType()+" a/c");
                table3.addCell("d");
                table3.addCell(expenseAccountDetails.getAmount().toString());
                table3.addCell("");
                table3.addCell("nominal account");

                table3.addCell("");
                table3.addCell("");
                table3.addCell("to "+expenseAccountDetails.getExpense().getPaymentMode()+" a/c");
                table3.addCell("c");
                table3.addCell("");
                table3.addCell(expenseAccountDetails.getAmount().toString());
                table3.addCell("real account");
            }

            PdfPTable table4 = new PdfPTable(7);

            Stream.of("Date", "Description", "Particulars", "Entry Type", "Debit Amount", "Credit Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table4.addCell(header);
                    });

            for (Payables payables: payablesList) {
                table4.addCell(payables.getPaymentDate().format(formatter));
                table4.addCell("purchased goods from " + (payables.getSupplier() == null  ?"" : payables.getSupplier().getFirstName()+" "+ payables.getSupplier().getLastName() + " worth rs " + payables.getAmount()));
                table4.addCell("purchase a/c");
                table4.addCell("d");
                table4.addCell(payables.getAmount().toString());
                table4.addCell("");
                table4.addCell("nominal account");

                table4.addCell("");
                table4.addCell("");
                table4.addCell("to cash a/c");
                table4.addCell("c");
                table4.addCell("");
                table4.addCell(payables.getAmount().toString());
                table4.addCell("real account");
            }

            PdfPTable table5 = new PdfPTable(7);

            Stream.of("Date", "Description", "Particulars", "Entry Type", "Debit Amount", "Credit Amount", "Account Type")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table5.addCell(header);
                    });

            for (PurchaseInvoice purchaseInvoice: purchaseInvoiceList) {
                table5.addCell(purchaseInvoice.getPurchaseDate().format(formatter));
                table5.addCell("purchased stock items worth rs " + purchaseInvoice.getPurchaseAmount() + " and paid through cash");
                table5.addCell("purchase a/c");
                table5.addCell("d");
                table5.addCell(purchaseInvoice.getPurchaseAmount().toString());
                table5.addCell("");
                table5.addCell("nominal account");

                table5.addCell("");
                table5.addCell("");
                table5.addCell("to cash a/c");
                table5.addCell("c");
                table5.addCell("");
                table5.addCell(purchaseInvoice.getPurchaseAmount().toString());
                table5.addCell("real account");
            }

            //Adding new page after every table
            document.add(table1);
            document.newPage();
            document.add(table2);
            document.newPage();
            document.add(table3);
            document.newPage();
            document.add(table4);
            document.newPage();
            document.add(table5);
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
                return "Description";
            case 2:
                return "Particulars";
            case 3:
                return "Entry Type";
            case 4:
                return "Debit Amount";
            case 5:
                return "Credit Amount";
            case 6:
                return "Account Type";
            default:
                return "";
        }
    }
}