package com.vitamin_market.vitamin_compare.mapper;

import com.vitamin_market.vitamin_compare.dto.VitaminDto;
import com.vitamin_market.vitamin_compare.entity.VitaminDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VitaminMapper {

    List<VitaminDto> mapListToDto(List<VitaminDocument> vitaminDocumentList);

    VitaminDto mapToDto(VitaminDocument vitaminDocument);
}
