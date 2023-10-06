package com.erp.journals.service;

import com.erp.journals.dto.LedgerDTO;
import com.erp.journals.entity.*;
import com.erp.journals.repository.*;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CreateLedgerService {
    Logger logger = LoggerFactory.getLogger(CreateLedgerService.class);

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    ReceivableRepository receivableRepository;

    @Autowired
    ExpenseTypesRepository expenseTypesRepository;

    @Autowired
    ExpenseAccountDetailsRepository expenseAccountDetailsRepository;

    @Autowired
    PayablesRepository payablesRepository;

    @Autowired
    PurchaseInvoiceRepository purchaseInvoiceRepository;

    @Scheduled(cron ="0 44 19 * * ?")
    public void executeAndSaveLedgers() {

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


            // Fetch data for the current month and year for sale and receivables
            List<Sale> saleList = saleRepository.findSalesByInvoiceDate(year, month);
            List<Receivables> receivablesList = receivableRepository.findReceivablesByPaymentDate(year, month);

            logger.info("Found {} sales invoices for {}_{}", saleList.size(), monthName, year);
            logger.info("Found {} receivables invoices for {}_{}", receivablesList.size(), monthName, year);

            // Create ledger entries
            List<LedgerDTO> saleLedgerDtoList = new ArrayList<>();

            for (Sale sale : saleList) {
                LedgerDTO ledgerDTO = new LedgerDTO();
                ledgerDTO.setDate(sale.getInvoiceDate());
                ledgerDTO.setParticulars("by " + (sale.extractPaymentMethod() == null ? "cash" : sale.extractPaymentMethod().toLowerCase()) + " a/c");
                ledgerDTO.setCreditAmount(sale.getPaidAmount());
                saleLedgerDtoList.add(ledgerDTO);
            }

            for (Receivables receivable : receivablesList) {
                LedgerDTO ledgerDTO = new LedgerDTO();
                ledgerDTO.setDate(receivable.getPaymentDate());
                ledgerDTO.setParticulars("by " + (receivable.getPaymentMode() == null ? "cash" : receivable.getPaymentMode().toLowerCase()) + " a/c");
                ledgerDTO.setCreditAmount(receivable.getAmount());
                saleLedgerDtoList.add(ledgerDTO);
            }

            // Sort ledgers by date
            saleLedgerDtoList.sort(Comparator.comparing(LedgerDTO::getDate));

            if(!(saleList.isEmpty() && receivablesList.isEmpty())) {
                createExcelForLedgers(saleLedgerDtoList, "Ledgers.xlsx", "Sale Ledger");
            }

            // Fetch data for the current month and year for payables and purchase invoice
            List<Payables> payablesList = payablesRepository.findPayablesByPaymentDate(year, month);
            List<PurchaseInvoice> purchaseInvoiceList = purchaseInvoiceRepository.findPurchaseInvoiceByPurchaseDate(year, month);

            logger.info("Found {} payable invoices for {}_{}", payablesList.size(), monthName, year);
            logger.info("Found {} purchase invoices for {}_{}", purchaseInvoiceList.size(), monthName, year);

            // Create ledger entries
            List<LedgerDTO> payableLedgerDtoList = new ArrayList<>();

            for (Payables payables : payablesList) {
                LedgerDTO ledgerDTO = new LedgerDTO();
                ledgerDTO.setDate(payables.getPaymentDate());
                ledgerDTO.setParticulars("to " + (payables.getPaymentMode() == null ? "cash" : payables.getPaymentMode().toLowerCase()) + " a/c");
                ledgerDTO.setDebitAmount(payables.getAmount());
                payableLedgerDtoList.add(ledgerDTO);
            }

            for (PurchaseInvoice purchaseInvoice : purchaseInvoiceList) {
                LedgerDTO ledgerDTO = new LedgerDTO();
                ledgerDTO.setDate(purchaseInvoice.getPurchaseDate());
                ledgerDTO.setParticulars("to " + (purchaseInvoice.extractPaymentMethod() == null ? "cash" : purchaseInvoice.extractPaymentMethod().toLowerCase()) + " a/c");
                ledgerDTO.setDebitAmount(purchaseInvoice.getPurchaseAmount());
                payableLedgerDtoList.add(ledgerDTO);
            }

            // Sort ledgers by date
            payableLedgerDtoList.sort(Comparator.comparing(LedgerDTO::getDate));

            if(!(purchaseInvoiceList.isEmpty() && payablesList.isEmpty())) {
                createExcelForLedgers(payableLedgerDtoList, "Ledgers.xlsx", "Purchase Ledger");
            }


            List<String> expenseTypes = expenseTypesRepository.findAllExpenseTypes();
            for(String expenseType : expenseTypes) {
                List<ExpenseAccountDetails> expenseAccountDetailsList = expenseAccountDetailsRepository.findExpenseAccountDetailsByExpenseTypeAndExpenseDate(expenseType, year, month);
                logger.info("Found {} expense invoices type {} for {}_{}", expenseAccountDetailsList.size(), expenseType, monthName, year);
                // Create ledger entries
                List<LedgerDTO> expenseLedgerDtoList = new ArrayList<>();

                for (ExpenseAccountDetails expenseAccountDetails : expenseAccountDetailsList) {
                    LedgerDTO ledgerDTO = new LedgerDTO();
                    ledgerDTO.setDate(expenseAccountDetails.getExpense().getPaymentDate());
                    ledgerDTO.setParticulars("to " + (expenseAccountDetails.getExpense().getPaymentMode() == null ? "cash" : expenseAccountDetails.getExpense().getPaymentMode().toLowerCase()) + " a/c");
                    ledgerDTO.setDebitAmount(expenseAccountDetails.getAmount());
                    expenseLedgerDtoList.add(ledgerDTO);
                }
                // Sort ledgers by date
                expenseLedgerDtoList.sort(Comparator.comparing(LedgerDTO::getDate));
                String sheetName = expenseType.replace('/', ' ').trim();
                if(!expenseAccountDetailsList.isEmpty()) {
                    createExcelForLedgers(expenseLedgerDtoList, "Ledgers.xlsx", sheetName);
                }
            }

//            List<Sale> sales = saleRepository.findSalesByPaymentMethodAndInvoiceDate("CASH",month,year);
//            List<Receivables> receivables = receivableRepository.findReceivablesByPaymentModeAndPaymentDate("Cash",year,month);
//            List<ExpenseAccountDetails> expenseAccountDetails = expenseAccountDetailsRepository.findExpenseAccountDetailsByExpenseTypeAndExpenseDate("CASH",year,month);
//            List<Payables> payables = payablesRepository.findPayablesByPaymentModeAndPaymentDate("Cash",year,month);
//            List<PurchaseInvoice> purchaseInvoices = purchaseInvoiceRepository.findPurchaseInvoiceByPaymentMethodAndPurchaseDate("CASH",year,month);
//
//            logger.info("Found {} cash ledgers from sales {}_{}", sales.size(), monthName, year);
//            logger.info("Found {} cash ledgers from receivables {}_{}", receivables.size(), monthName, year);
//            logger.info("Found {} cash ledgers from expense {}_{}", expenseAccountDetails.size(), monthName, year);
//            logger.info("Found {} cash ledgers from payables {}_{}", payables.size(), monthName, year);
//            logger.info("Found {} cash ledgers from purchases {}_{}", purchaseInvoices.size(), monthName, year);
//
//            List<LedgerDTO> cashLedgerDtoList = new ArrayList<>();
//
//            for (Sale sale : saleList) {
//                LedgerDTO ledgerDTO = new LedgerDTO();
//                ledgerDTO.setDate(sale.getInvoiceDate());
//                ledgerDTO.setParticulars("to sales a/c");
//                ledgerDTO.setDebitAmount(sale.getPaidAmount());
//                cashLedgerDtoList.add(ledgerDTO);
//            }
//
//            for (Receivables receivable : receivablesList) {
//                LedgerDTO ledgerDTO = new LedgerDTO();
//                ledgerDTO.setDate(receivable.getPaymentDate());
//                ledgerDTO.setParticulars("to sales a/c");
//                ledgerDTO.setDebitAmount(receivable.getAmount());
//                cashLedgerDtoList.add(ledgerDTO);
//            }
//
//            for (ExpenseAccountDetails expenseAccountDetail : expenseAccountDetails) {
//                LedgerDTO ledgerDTO = new LedgerDTO();
//                ledgerDTO.setDate(expenseAccountDetail.getExpense().getPaymentDate());
//                ledgerDTO.setParticulars("by " + (expenseAccountDetail.getExpense().getPaymentMode() == null ? "expense" : expenseAccountDetail.getExpenseTypes().getType().toLowerCase()) + " a/c");
//                ledgerDTO.setCreditAmount(expenseAccountDetail.getAmount());
//                cashLedgerDtoList.add(ledgerDTO);
//            }
//
//            for (Payables payable : payables) {
//                LedgerDTO ledgerDTO = new LedgerDTO();
//                ledgerDTO.setDate(payable.getPaymentDate());
//                ledgerDTO.setParticulars("by purchase a/c");
//                ledgerDTO.setCreditAmount(payable.getAmount());
//                cashLedgerDtoList.add(ledgerDTO);
//            }
//
//            for (PurchaseInvoice purchaseInvoice : purchaseInvoices) {
//                LedgerDTO ledgerDTO = new LedgerDTO();
//                ledgerDTO.setDate(purchaseInvoice.getPurchaseDate());
//                ledgerDTO.setParticulars("by purchase a/c");
//                ledgerDTO.setCreditAmount(purchaseInvoice.getPurchaseAmount());
//                cashLedgerDtoList.add(ledgerDTO);
//            }
//
//            // Sort ledgers by date
//            cashLedgerDtoList.sort(Comparator.comparing(LedgerDTO::getDate));
//
//            createExcelForLedgers(cashLedgerDtoList, "Ledgers.xlsx", "Cash Ledger");


//            createPdfsForLedgers(saleLedgerDtoList,"sale_ledger.pdf");

            calendar.add(Calendar.MONTH, 1);
            startDate = new Date(calendar.getTimeInMillis());
        }
    }
    public void createExcelForLedgers(List<LedgerDTO> ledgerDTOList, String existingFilePath, String sheetName) {

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
            }

            // Find the last row index
            int lastRow = sheet.getLastRowNum();

            // Create header row (if it doesn't exist)
            if (sheet.getLastRowNum() < lastRow + 1) {
                createHeaderRow(sheet, lastRow + 2);
            }

            int rowNum = lastRow + 2; // Start row index

            for (LedgerDTO ledgerDTO : ledgerDTOList) {
                Row row = sheet.createRow(rowNum++);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
                row.createCell(0).setCellValue(ledgerDTO.getDate().format(formatter));
                row.createCell(1).setCellValue(ledgerDTO.getParticulars());
                Integer debitAmount = ledgerDTO.getDebitAmount();
                if (debitAmount != null) {
                    row.createCell(2).setCellValue(debitAmount);
                }
                Integer creditAmount = ledgerDTO.getCreditAmount();
                if (creditAmount != null) {
                    row.createCell(3).setCellValue(creditAmount);
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(existingFilePath)) {
                workbook.write(outputStream);
                logger.trace("Excel file updated for Sale: " + existingFilePath);
            }
        } catch (IOException e) {
            logger.error("error is " + e);
        }
    }

    public void createPdfsForLedgers(List<LedgerDTO> ledgerDTOList, String pdfFileName) {

        try {
            Document document = new Document();

            // Check if the PDF file already exists
            if (new File(pdfFileName).exists()) {
                PdfWriter.getInstance(document, new FileOutputStream(pdfFileName, true)); // Append mode
            }
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));


            document.open();

            PdfPTable table = new PdfPTable(4);
            Stream.of("Date", "Particulars", "Debit Amount", "Credit Amount")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

            for (LedgerDTO ledgerDTO : ledgerDTOList) {
                table.addCell(ledgerDTO.getDate().format(formatter));
                table.addCell(ledgerDTO.getParticulars());
                Integer debitAmount = ledgerDTO.getDebitAmount();
                if (debitAmount != null) {
                    table.addCell(debitAmount.toString());
                }
                else{
                    table.addCell("");
                }
                Integer creditAmount = ledgerDTO.getCreditAmount();
                if (creditAmount != null) {
                    table.addCell(creditAmount.toString());
                }
                else {
                    table.addCell("");
                }
            }

            //Adding new page after every table
            document.add(table);
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
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));
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
        for (int i = 0; i < 4; i++) {
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