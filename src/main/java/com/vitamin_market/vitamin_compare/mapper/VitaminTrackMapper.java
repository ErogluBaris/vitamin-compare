package com.vitamin_market.vitamin_compare.mapper;

import com.vitamin_market.vitamin_compare.dto.VitaminTrackDto;
import com.vitamin_market.vitamin_compare.entity.VitaminTrackDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VitaminTrackMapper {

    List<VitaminTrackDocument> mapListToDocument(List<VitaminTrackDto> dtoList);

    @Mapping(target = "id", source = "vitaminId")
    VitaminTrackDocument mapToDocument(VitaminTrackDto dto);
}
