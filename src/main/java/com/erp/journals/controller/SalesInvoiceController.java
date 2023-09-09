package com.erp.journals.controller;

import com.erp.journals.dto.SalesInvoiceDTO;
import com.erp.journals.service.SalesInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
@RestController
public class SalesInvoiceController {
    @Autowired
    SalesInvoiceService salesInvoiceService;

    /**
     * Returns all the salesInvoice objects.
     * URL : "http://localhost:8080/SalesInvoices"
     * @return  All salesInvoices as an arrayList of salesInvoice JSON objects.
     */
    @GetMapping("/SalesInvoices")
    public List<SalesInvoiceDTO> getAllSalesInvoices(){
        return salesInvoiceService.getSalesInvoices();
    }

    /**
     * Returns the salesInvoice object with given salesInvoiceId.
     * URL : "http://localhost:8080/getSalesInvoiceById/"
     * @param salesInvoiceId as an input.
     * @return ResponseEntity with salesInvoice information if found, or 404 if not found.
     * @throws if SalesInvoice object not found with given salesInvoiceId.
     */
    @GetMapping("/getSalesInvoiceById/{salesInvoiceId}")
    public ResponseEntity<SalesInvoiceDTO> getSalesInvoiceById(@PathVariable(value = "salesInvoiceId") int salesInvoiceId) {
        try {
            SalesInvoiceDTO salesInvoiceDTO = salesInvoiceService.getSalesInvoiceById(salesInvoiceId);
            return new ResponseEntity<>(salesInvoiceDTO, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates the salesInvoice object.
     * URL : "http://localhost8080/createSalesInvoice"
     * @param  salesInvoiceDTO object as an input.
     * @return  salesInvoice JSON object.
     */
    @PostMapping("/createSalesInvoice")
    public SalesInvoiceDTO createSalesInvoice(@RequestBody SalesInvoiceDTO salesInvoiceDTO){
        return salesInvoiceService.createSalesInvoice(salesInvoiceDTO);
    }

    /**
     * Updates the salesInvoice object.
     * URL : "http://localhost8080/updateSalesInvoice"
     * @param  salesInvoiceDTO object as an input.
     * @return ResponseEntity with salesInvoice information if updated, or 404 if not found.
     * @throws  if SalesInvoice object not found.
     */
    @PutMapping("/updateSalesInvoice")
    public ResponseEntity<SalesInvoiceDTO> updateSalesInvoice(@RequestBody SalesInvoiceDTO salesInvoiceDTO){
        try{
            SalesInvoiceDTO updatedSalesInvoiceDTO = salesInvoiceService.updateSalesInvoice(salesInvoiceDTO);
            return new ResponseEntity<>(updatedSalesInvoiceDTO,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    /**
     * Deletes the salesInvoice object with specific id
     * URL : "http://localhost8080/deleteSalesInvoiceById/"
     * @param salesInvoiceId as an input.
     * @return AddResponse with salesInvoiceId which is deleted.
     */
    @DeleteMapping("/deleteSalesInvoiceById/{salesInvoiceId}")
    public AddResponse[] deleteSalesInvoiceById(@PathVariable(value = "salesInvoiceId") int salesInvoiceId){
        return salesInvoiceService.deleteSalesInvoiceById(salesInvoiceId);
    }
}
