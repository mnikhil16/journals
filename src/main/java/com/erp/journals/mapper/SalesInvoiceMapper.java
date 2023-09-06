package com.erp.journals.mapper;

import com.erp.journals.entity.SalesInvoice;
import com.erp.journals.dto.SalesInvoiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting between SalesInvoice entities and SalesInvoiceDTOs.
 * Uses MapStruct and Spring's component model for mapping.
 */
@Mapper(componentModel = "spring")
public interface SalesInvoiceMapper {

    // Singleton instance of the SalesInvoiceMapper interface
    SalesInvoiceMapper instance = Mappers.getMapper(SalesInvoiceMapper.class);

    /**
     * Converts a SalesInvoice entity to a SalesInvoiceDTO.
     *
     * @param salesInvoice The SalesInvoice entity to be converted.
     * @return The corresponding SalesInvoiceDTO.
     */
    SalesInvoiceDTO modelToDto(SalesInvoice salesInvoice);

    /**
     * Converts a SalesInvoiceDTO to a SalesInvoice entity.
     *
     * @param salesInvoiceDTO The SalesInvoiceDTO to be converted.
     * @return The corresponding SalesInvoice entity.
     */
    SalesInvoice dtoToModel(SalesInvoiceDTO salesInvoiceDTO);

    /**
     * Converts a list of SalesInvoice entities to a list of SalesInvoiceDTOs.
     *
     * @param salesInvoiceList The list of SalesInvoice entities to be converted.
     * @return The corresponding list of SalesInvoiceDTOs.
     */
    List<SalesInvoiceDTO> modelToDtoList(List<SalesInvoice> salesInvoiceList);

    /**
     * Converts a list of SalesInvoiceDTOs to a list of SalesInvoice entities.
     *
     * @param salesInvoiceDTOList The list of SalesInvoiceDTOs to be converted.
     * @return The corresponding list of SalesInvoice entities.
     */
    List<SalesInvoice> dtoToModelList(List<SalesInvoiceDTO> salesInvoiceDTOList);
}