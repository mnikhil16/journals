package com.erp.journals.service;

import com.erp.journals.controller.AddResponse;
import com.erp.journals.dto.SalesInvoiceDTO;
import com.erp.journals.entity.SalesInvoice;
import com.erp.journals.mapper.SalesInvoiceMapper;
import com.erp.journals.repository.SalesInvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SalesInvoiceService {
    @Autowired
    SalesInvoiceRepository salesInvoiceRepository;

    @Autowired
    JournalEntryService journalEntryService;
    Logger logger = LoggerFactory.getLogger(SalesInvoiceService.class);

    /**
     * Get all the salesInvoice information.
     *
     * @return All the SalesInvoice objects.
     */
    public List<SalesInvoiceDTO> getSalesInvoices(){
        logger.trace("Get all Sales Invoices service is invoked.");
        List<SalesInvoice> salesInvoiceList = salesInvoiceRepository.findAll();
        List<SalesInvoiceDTO> salesInvoiceDTOList =  SalesInvoiceMapper.instance.modelToDtoList(salesInvoiceList);
        return salesInvoiceDTOList;
    }

    /**
     * Get salesInvoice information by the specified salesInvoice ID.
     *
     * @param salesInvoiceId The ID of the salesInvoice to retrieve.
     * @return The SalesInvoice object corresponding to the given ID.
     */
    public SalesInvoiceDTO getSalesInvoiceById(int salesInvoiceId){
        logger.trace("Get Sales Invoice by Id service is invoked.");
        SalesInvoice salesInvoiceEntity = salesInvoiceRepository.findById(salesInvoiceId).get();
        SalesInvoiceDTO salesInvoiceDTO = SalesInvoiceMapper.instance.modelToDto(salesInvoiceEntity);
        return salesInvoiceDTO;
    }

    /**
     * Create a new salesInvoice with the provided SalesInvoice object.
     *
     * @param salesInvoiceDTO The SalesInvoiceDTO object representing the salesInvoice to be created.
     * @return The newly created SalesInvoice object with a generated ID.
     */
    public SalesInvoiceDTO createSalesInvoice(SalesInvoiceDTO salesInvoiceDTO) {
        logger.trace("Create Sales Invoice service is invoked.");
        SalesInvoice salesInvoiceEntity = SalesInvoiceMapper.instance.dtoToModel(salesInvoiceDTO);
        salesInvoiceEntity = salesInvoiceRepository.save(salesInvoiceEntity);
        SalesInvoiceDTO salesInvoiceDTO1 = SalesInvoiceMapper.instance.modelToDto(salesInvoiceEntity);
        journalEntryService.createJournalEntry(salesInvoiceDTO1);
        SalesInvoiceDTO createdSalesInvoiceDTO = SalesInvoiceMapper.instance.modelToDto(salesInvoiceEntity);
        return createdSalesInvoiceDTO;
    }

    /**
     * Update salesInvoice information with the provided SalesInvoice object.
     *
     * @param salesInvoiceDTO The SalesInvoiceDTO object representing the salesInvoice to be updated.
     * @return The updated SalesInvoice object.
     */
    public SalesInvoiceDTO updateSalesInvoice(SalesInvoiceDTO salesInvoiceDTO){
        logger.trace("Update Sales Invoice service is invoked.");
        SalesInvoice salesInvoiceEntity = SalesInvoiceMapper.instance.dtoToModel(salesInvoiceDTO);
        SalesInvoice salesInvoice = salesInvoiceRepository.save(salesInvoiceEntity);
        SalesInvoiceDTO salesInvoiceDTO1 = SalesInvoiceMapper.instance.modelToDto(salesInvoice);
        journalEntryService.updateJournalEntry(salesInvoiceDTO1);
        SalesInvoiceDTO updatedSalesInvoiceDTO = SalesInvoiceMapper.instance.modelToDto(salesInvoiceEntity);
        return updatedSalesInvoiceDTO;
    }

    /**
     * Delete a salesInvoice with the provided salesInvoiceId.
     *
     * @param salesInvoiceId The ID of the salesInvoice to delete.
     * @return An AddResponse indicating the deletion result.
     */
    public AddResponse[] deleteSalesInvoiceById(int salesInvoiceId){
        logger.trace("Delete Sales Invoice service is invoked.");
        SalesInvoice salesInvoiceEntity = salesInvoiceRepository.findById(salesInvoiceId).get();
        SalesInvoiceDTO salesInvoiceDTO = SalesInvoiceMapper.instance.modelToDto(salesInvoiceEntity);
        AddResponse addResponseJournalEntry = journalEntryService.deleteJournalEntry(salesInvoiceDTO);
        salesInvoiceRepository.deleteById(salesInvoiceId);
        AddResponse addResponseSalesInvoice = new AddResponse();
        addResponseSalesInvoice.setMsg("SalesInvoice deleted");
        addResponseSalesInvoice.setId(salesInvoiceId);
        AddResponse[] addResponses = new AddResponse[]{new AddResponse(), new AddResponse()};
        return addResponses;
    }
}