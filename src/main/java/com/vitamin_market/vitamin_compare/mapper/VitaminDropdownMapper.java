package com.vitamin_market.vitamin_compare.mapper;

import com.vitamin_market.vitamin_compare.dto.VitaminDropdownDto;
import com.vitamin_market.vitamin_compare.entity.VitaminDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VitaminDropdownMapper {

    List<VitaminDropdownDto> toDropdownList(List<VitaminDocument> vitaminDocument);

    VitaminDropdownDto toDropdown(VitaminDocument vitaminDocument);
}
