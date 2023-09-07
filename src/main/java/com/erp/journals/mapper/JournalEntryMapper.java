package com.erp.journals.mapper;

import com.erp.journals.dto.JournalEntryDTO;
import com.erp.journals.dto.SalesInvoiceDTO;
import com.erp.journals.entity.JournalEntry;
import com.erp.journals.entity.SalesInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {
    JournalEntryMapper instance = Mappers.getMapper(JournalEntryMapper.class);

    @Mapping(source = "salesInvoice", target = "salesInvoiceDTO")
    JournalEntryDTO modelToDto(JournalEntry journalEntry);

    SalesInvoiceDTO modelToDto(SalesInvoice salesInvoice);

    @Mapping(source = "salesInvoiceDTO", target = "salesInvoice")
    JournalEntry dtoToModel(JournalEntryDTO journalEntryDTO);

    SalesInvoice dtoToModel(SalesInvoiceDTO salesInvoiceDTO);

    List<JournalEntryDTO> modelToDtoList(List<JournalEntry> journalEntryList);

    List<JournalEntry> dtoToModelList(List<JournalEntryDTO> journalEntryDTOList);
}
