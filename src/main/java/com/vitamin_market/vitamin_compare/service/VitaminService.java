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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VitaminService {

    private final VitaminRepository vitaminRepository;
    private final VitaminTrackService vitaminTrackService;
    private final VitaminMapper vitaminMapper;
    private final VitaminDropdownMapper vitaminDropdownMapper;
    private final MongoTemplate mongoTemplate;

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
        if (titleLikeText == null || titleLikeText.contains("$") || titleLikeText.contains("{") || titleLikeText.contains("}")) {
            throw new IllegalArgumentException("Invalid input");
        }

        List<String> keywords = Arrays.stream(titleLikeText.trim().split("\\s+"))
                .filter(word -> !word.isBlank())
                .map(Pattern::quote)
                .collect(Collectors.toList());

        if (keywords.isEmpty()) {
            return new ArrayList<>();
        }

        return vitaminDropdownMapper.toDropdownList(findByTitleContainingAll(keywords));
    }

    public List<VitaminDocument> findByTitleContainingAll(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"$and\": [");
        for (int i = 0; i < keywords.size(); i++) {
            String escaped = escapeRegex(keywords.get(i));
            sb.append("{ \"title\": { \"$regex\": \".*")
                    .append(escaped)
                    .append(".*\", \"$options\": \"i\" } }");
            if (i < keywords.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] }");

        BasicQuery query = new BasicQuery(sb.toString());
        query.limit(20);

        return mongoTemplate.find(query, VitaminDocument.class);
    }

    public List<VitaminDto> getPopular() {
        Set<String> popularVitaminIds = vitaminTrackService.findPopular().stream()
                .map(VitaminTrackDocument::getId)
                .collect(Collectors.toSet());

        return vitaminMapper.mapListToDto(vitaminRepository.findAllById(popularVitaminIds));
    }

    public static String escapeRegex(String input) {
        return input.replaceAll("([\\\\.*+?\\^${}()|\\[\\]])", "\\\\$1");
    }

}
