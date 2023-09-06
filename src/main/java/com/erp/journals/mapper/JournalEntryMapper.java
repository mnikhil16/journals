package com.erp.journals.mapper;

import com.erp.journals.dto.JournalEntryDTO;
import com.erp.journals.entity.JournalEntry;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {
    JournalEntryMapper instance = Mappers.getMapper(JournalEntryMapper.class);

    JournalEntryDTO modelToDto(JournalEntry journalEntry);

    JournalEntry dtoToModel(JournalEntryDTO journalEntryDTO);

    List<JournalEntryDTO> modelToDtoList(List<JournalEntry> journalEntryList);

    List<JournalEntry> dtoToModelList(List<JournalEntryDTO> journalEntryDTOList);
}
