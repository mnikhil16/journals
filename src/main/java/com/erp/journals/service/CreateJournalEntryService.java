package com.erp.journals.service;


import com.erp.journals.entity.*;
import com.erp.journals.repository.*;
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

    @Scheduled(cron ="20 48 11 * * ?")
    public void executeAndSaveJournalEntries() throws IOException {

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


            // Fetch sales data for the current month and year
//            List<Sale> saleList = saleRepository.findSalesByInvoiceDate(year, month);
//            List<Receivables> receivablesList = receivableRepository.findReceivablesByInvoiceDate(year, month);
            List<ExpenseAccountDetails> expenseAccountDetailsList = expenseAccountDetailsRepository.findExpenseAccountDetailsByExpenseDate(year, month);
//            List<Payables> payablesList = payablesRepository.findPayablesByPaymentDate(year, month);
//            List<PurchaseInvoice> purchaseInvoiceList = purchaseInvoiceRepository.findPurchaseInvoiceByPurchaseDate(year, month);

//            logger.info("Found {} sales invoices for {}_{}", saleList.size(), monthName, year);
//            logger.info("Found {} sales invoices for {}_{}", receivablesList.size(), monthName, year);
            logger.info("Found {} expenses for {}_{}", expenseAccountDetailsList.size(), monthName, year);
//            logger.info("Found {} payables for {}_{}", payablesList.size(), monthName, year);
//            logger.info("Found {} purchases for {}_{}", purchaseInvoiceList.size(), monthName, year);



//            if(!saleList.isEmpty()) {
//                createExcelForJournalEntriesSale(month, year, saleList, "JournalEntriesForSale.xlsx");
//                createPdfsForJournalEntriesSale(month, year, saleList);
//            }

//            if(!receivablesList.isEmpty()) {
//                createExcelForJournalEntriesReceivable(month, year, receivablesList, "JournalEntriesForReceivable.xlsx");
//                createPdfsForJournalEntriesReceivable(month, year, receivablesList);
//            }


            if(!expenseAccountDetailsList.isEmpty()) {
                createExcelForJournalEntriesExpense(month, year, expenseAccountDetailsList, "JournalEntriesForExpense.xlsx");
                createPdfsForJournalEntriesExpense(month, year, expenseAccountDetailsList);
            }

//            if(!payablesList.isEmpty()) {
//                createExcelForJournalEntriesPayable(month, year, payablesList, "JournalEntriesForPayable.xlsx");
//                createPdfsForJournalEntriesPayable(month, year, payablesList);
//            }

//            if(!purchaseInvoiceList.isEmpty()) {
//                createExcelForJournalEntriesPurchase(month, year, purchaseInvoiceList, "JournalEntriesForPurchase.xlsx");
//                createPdfsForJournalEntriesPurchase(month, year, purchaseInvoiceList);
//            }

            calendar.add(Calendar.MONTH, 1);
            startDate = new Date(calendar.getTimeInMillis());
        }
    }

    public void createExcelForJournalEntriesSale(int month, int year, List<Sale> saleList, String existingFilePath) {
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

    public void createPdfsForJournalEntriesSale(int month, int year, List<Sale> saleList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_sale_" + monthName + "_" + year + ".pdf";

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

    public void createExcelForJournalEntriesReceivable(int month, int year, List<Receivables> receivablesList, String existingFilePath) {
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

            for (Receivables receivables: receivablesList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(receivables.getPaymentDate().format(formatter));
                row1.createCell(1).setCellValue("sold goods to " + (receivables.getCustomer() == null  ?"" : receivables.getCustomer().getFirstName() + " for rs " + receivables.getAmount()));
                row1.createCell(2).setCellValue("cash a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(receivables.getAmount());
                row1.createCell(5).setCellValue("real account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to sales a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(receivables.getAmount());
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

    public void createPdfsForJournalEntriesReceivable(int month, int year, List<Receivables> receivablesList) {

        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_receivable_" + monthName + "_" + year + ".pdf";

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

            for (Receivables receivables: receivablesList) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                table.addCell(receivables.getPaymentDate().format(formatter));
                table.addCell("sold goods to " + (receivables.getCustomer() == null  ?"" : receivables.getCustomer().getFirstName() + " for rs " + receivables.getAmount()));
                table.addCell("cash a/c");
                table.addCell("d");
                table.addCell(receivables.getAmount().toString());
                table.addCell("real account");

                table.addCell("");
                table.addCell("");
                table.addCell("to sales a/c");
                table.addCell("c");
                table.addCell(receivables.getAmount().toString());
                table.addCell("nominal account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExcelForJournalEntriesExpense(int month, int year, List<ExpenseAccountDetails> expenseAccountDetailsList, String existingFilePath) {
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

            for (ExpenseAccountDetails expenseAccountDetails: expenseAccountDetailsList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(expenseAccountDetails.getExpense().getPaymentDate().format(formatter));
                row1.createCell(1).setCellValue("Paid for "+(expenseAccountDetails.getExpenseTypes() == null  ?"" : expenseAccountDetails.getExpenseTypes().getType())+" to " + (expenseAccountDetails.getDescription() == null ? "":expenseAccountDetails.getDescription()) + " rs " + expenseAccountDetails.getAmount());
                row1.createCell(2).setCellValue(expenseAccountDetails.getExpenseTypes().getType()+" a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(expenseAccountDetails.getAmount());
                row1.createCell(5).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to "+expenseAccountDetails.getExpense().getPaymentMode()+" a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(expenseAccountDetails.getAmount());
                row2.createCell(5).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesExpense(int month, int year, List<ExpenseAccountDetails> expenseAccountDetailsList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_expense_" + monthName + "_" + year + ".pdf";

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

            for (ExpenseAccountDetails expenseAccountDetails: expenseAccountDetailsList) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                table.addCell(expenseAccountDetails.getExpense().getPaymentDate().format(formatter));
                table.addCell("Paid for "+(expenseAccountDetails.getExpenseTypes() == null  ?"" : expenseAccountDetails.getExpenseTypes().getType())+" to " + (expenseAccountDetails.getDescription() == null ? "":expenseAccountDetails.getDescription()) + " rs " + expenseAccountDetails.getAmount());
                table.addCell(expenseAccountDetails.getExpenseTypes().getType()+" a/c");
                table.addCell("d");
                table.addCell(expenseAccountDetails.getAmount().toString());
                table.addCell("nominal account");

                table.addCell("");
                table.addCell("");
                table.addCell("to "+expenseAccountDetails.getExpense().getPaymentMode()+" a/c");
                table.addCell("c");
                table.addCell(expenseAccountDetails.getAmount().toString());
                table.addCell("real account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExcelForJournalEntriesPayable(int month, int year, List<Payables> payablesList, String existingFilePath) {
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

            for (Payables payables: payablesList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(payables.getPaymentDate().format(formatter));
                row1.createCell(1).setCellValue("purchased goods from " + (payables.getSupplier() == null  ?"" : payables.getSupplier().getFirstName()+" "+ payables.getSupplier().getLastName() + " worth rs " + payables.getAmount()));
                row1.createCell(2).setCellValue("purchase a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(payables.getAmount());
                row1.createCell(5).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to cash a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(payables.getAmount());
                row2.createCell(5).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesPayable(int month, int year, List<Payables> payablesList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_payables_" + monthName + "_" + year + ".pdf";

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

            for (Payables payables: payablesList) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                table.addCell(payables.getPaymentDate().format(formatter));
                table.addCell("purchased goods from " + (payables.getSupplier() == null  ?"" : payables.getSupplier().getFirstName()+" "+ payables.getSupplier().getLastName() + " worth rs " + payables.getAmount()));
                table.addCell("purchase a/c");
                table.addCell("d");
                table.addCell(payables.getAmount().toString());
                table.addCell("nominal account");

                table.addCell("");
                table.addCell("");
                table.addCell("to cash a/c");
                table.addCell("c");
                table.addCell(payables.getAmount().toString());
                table.addCell("real account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createExcelForJournalEntriesPurchase(int month, int year, List<PurchaseInvoice> purchaseInvoiceList, String existingFilePath) {
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

            for (PurchaseInvoice purchaseInvoice: purchaseInvoiceList) {
                Row row1 = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row1.createCell(0).setCellValue(purchaseInvoice.getPurchaseDate().format(formatter));
                row1.createCell(1).setCellValue("purchased stock items worth rs " + purchaseInvoice.getPurchaseAmount() + " and paid through cash");
                row1.createCell(2).setCellValue("purchase a/c");
                row1.createCell(3).setCellValue("d");
                row1.createCell(4).setCellValue(purchaseInvoice.getPurchaseAmount());
                row1.createCell(5).setCellValue("nominal account");

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue("");
                row2.createCell(1).setCellValue("");
                row2.createCell(2).setCellValue("to cash a/c");
                row2.createCell(3).setCellValue("c");
                row2.createCell(4).setCellValue(purchaseInvoice.getPurchaseAmount());
                row2.createCell(5).setCellValue("real account");
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForJournalEntriesPurchase(int month, int year, List<PurchaseInvoice> purchaseInvoiceList) {
        Month monthNumber = Month.of(month);
        String monthName = monthNumber.toString();
        String pdfFileName = "journal_entries_for_purchases_" + monthName + "_" + year + ".pdf";

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

            for (PurchaseInvoice purchaseInvoice: purchaseInvoiceList) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                table.addCell(purchaseInvoice.getPurchaseDate().format(formatter));
                table.addCell("purchased stock items worth rs " + purchaseInvoice.getPurchaseAmount() + " and paid through cash");
                table.addCell("purchase a/c");
                table.addCell("d");
                table.addCell(purchaseInvoice.getPurchaseAmount().toString());
                table.addCell("nominal account");

                table.addCell("");
                table.addCell("");
                table.addCell("to cash a/c");
                table.addCell("c");
                table.addCell(purchaseInvoice.getPurchaseAmount().toString());
                table.addCell("real account");

            }

            document.add(table);
            document.close();

            logger.trace("PDF file created: " + pdfFileName);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}