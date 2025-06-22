package com.vitamin_market.vitamin_compare.service;

import com.vitamin_market.vitamin_compare.annotation.TrackView;
import com.vitamin_market.vitamin_compare.dto.VitaminDropdownDto;
import com.vitamin_market.vitamin_compare.dto.VitaminDto;
import com.vitamin_market.vitamin_compare.entity.VitaminDocument;
import com.vitamin_market.vitamin_compare.entity.VitaminTrackDocument;
import com.vitamin_market.vitamin_compare.mapper.VitaminDropdownMapper;
import com.vitamin_market.vitamin_compare.mapper.VitaminMapper;
import com.vitamin_market.vitamin_compare.repository.VitaminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VitaminService {

    private final VitaminRepository vitaminRepository;
    private final VitaminTrackService vitaminTrackService;
    private final VitaminMapper vitaminMapper;
    private final VitaminDropdownMapper vitaminDropdownMapper;

    @TrackView
    public VitaminDto getById(String id) {
        return vitaminRepository.findById(id)
                .map(vitaminMapper::mapToDto)
        .orElse(null);
    }

    public Map<String, VitaminDocument> getByIds(Set<String> ids) {
        return vitaminRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(VitaminDocument::getId, Function.identity()));
    }

    public List<VitaminDropdownDto> getDropdown(String titleLikeText) {
        String regex = ".*" + titleLikeText + ".*";
        Pageable pageable = PageRequest.of(0, 20);
        return vitaminDropdownMapper.toDropdownList(vitaminRepository.findAllByTitleLikeIgnoreCase(regex, pageable));
    }

    public List<VitaminDto> getPopular() {
        Set<String> popularVitaminIds = vitaminTrackService.findPopular().stream()
                .map(VitaminTrackDocument::getId)
                .collect(Collectors.toSet());

        return vitaminMapper.mapListToDto(vitaminRepository.findAllById(popularVitaminIds));
    }
}
