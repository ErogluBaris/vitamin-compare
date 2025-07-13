package com.vitamin_market.vitamin_compare.service;

import com.vitamin_market.vitamin_compare.domain.CompareElement;
import com.vitamin_market.vitamin_compare.domain.CompareHeader;
import com.vitamin_market.vitamin_compare.domain.CompareRow;
import com.vitamin_market.vitamin_compare.domain.ContentCompare;
import com.vitamin_market.vitamin_compare.entity.Content;
import com.vitamin_market.vitamin_compare.entity.VitaminDocument;
import com.vitamin_market.vitamin_compare.enums.CompareEnum;
import com.vitamin_market.vitamin_compare.enums.ContentTypeEnum;
import com.vitamin_market.vitamin_compare.regex.ContentMatcher;
import com.vitamin_market.vitamin_compare.request.VitaminCompareRequest;
import com.vitamin_market.vitamin_compare.response.VitaminCompareResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VitaminCompareService {

    private static final String DEFAULT_AMOUNT = "-";

    private final VitaminService vitaminService;

    public VitaminCompareResponse compare(VitaminCompareRequest request) {
        Map<String, VitaminDocument> vitaminMap = vitaminService.getByIds(
                Set.of(request.getFirstId(), request.getSecondId()));

        VitaminDocument firstElement = vitaminMap.get(request.getFirstId());
        VitaminDocument secondElement = vitaminMap.get(request.getSecondId());

        Map<ContentTypeEnum, Set<String>> contentEnumMap = new EnumMap<>(ContentTypeEnum.class);
        for (ContentTypeEnum contentTypeEnum : ContentTypeEnum.values()) {
            contentEnumMap.put(contentTypeEnum, new HashSet<>());
        }
        Map<String, Content> firstElementContentMap = new HashMap<>();
        Map<String, Content>  secondElementContentMap = new HashMap<>();

        if (firstElement.getVitaminContent() != null) {
            contentEnumMap.put(ContentTypeEnum.VITAMIN_CONTENT,
                    firstElement.getVitaminContent().stream().map(Content::getName).collect(Collectors.toSet()));
            firstElementContentMap.putAll(firstElement.getVitaminContent().stream()
                    .collect(Collectors.toMap(Content::getName, Function.identity())));
        }
        if (firstElement.getMineralContent() != null) {
            contentEnumMap.put(ContentTypeEnum.MINERAL_CONTENT,
                    firstElement.getMineralContent().stream().map(Content::getName).collect(Collectors.toSet()));
            firstElementContentMap.putAll(firstElement.getMineralContent().stream()
                    .collect(Collectors.toMap(Content::getName, Function.identity())));
        }
        if (firstElement.getOtherContent() != null) {
            contentEnumMap.put(ContentTypeEnum.OTHER_CONTENT,
                    firstElement.getOtherContent().stream().map(Content::getName).collect(Collectors.toSet()));
            firstElementContentMap.putAll(firstElement.getOtherContent().stream()
                    .collect(Collectors.toMap(Content::getName, Function.identity())));
        }

        if (secondElement.getVitaminContent() != null) {
            contentEnumMap.get(ContentTypeEnum.VITAMIN_CONTENT).addAll(
                    secondElement.getVitaminContent().stream().map(Content::getName).collect(Collectors.toSet()));
            secondElementContentMap.putAll(secondElement.getVitaminContent().stream()
                    .collect(Collectors.toMap(Content::getName, Function.identity())));
        }
        if (secondElement.getMineralContent() != null) {
            contentEnumMap.get(ContentTypeEnum.MINERAL_CONTENT).addAll(
                    secondElement.getMineralContent().stream().map(Content::getName).collect(Collectors.toSet()));
            secondElementContentMap.putAll(secondElement.getMineralContent().stream()
                    .collect(Collectors.toMap(Content::getName, Function.identity())));
        }
        if (secondElement.getOtherContent() != null) {
            contentEnumMap.get(ContentTypeEnum.OTHER_CONTENT).addAll(
                    secondElement.getOtherContent().stream().map(Content::getName).collect(Collectors.toSet()));
            secondElementContentMap.putAll(secondElement.getOtherContent().stream()
                    .collect(Collectors.toMap(Content::getName, Function.identity())));
        }

        VitaminCompareResponse vitaminCompareResponse = new VitaminCompareResponse();
        vitaminCompareResponse.setCompareResults(new ArrayList<>());
        Set<String> comparedContentNames = new HashSet<>();
        for (ContentTypeEnum contentTypeEnum :
                List.of(ContentTypeEnum.VITAMIN_CONTENT, ContentTypeEnum.MINERAL_CONTENT, ContentTypeEnum.OTHER_CONTENT)) {
            List<CompareRow> compareRows = new ArrayList<>();
            Set<String> vitaminContentNames = contentEnumMap.get(contentTypeEnum) == null
                    ? new HashSet<>() : contentEnumMap.get(contentTypeEnum);
            for (String key : vitaminContentNames) {
                if (comparedContentNames.contains(key)) {
                    continue;
                }
                Content firstVitaminContent = getContentWithNullControl(firstElementContentMap, key);
                Content secondElementContent = getContentWithNullControl(secondElementContentMap, key);
                compareRows.add(new CompareRow(key, compareElements(firstVitaminContent, secondElementContent)));
                comparedContentNames.add(key);
            }
            vitaminCompareResponse.getCompareResults().add(new ContentCompare(contentTypeEnum.getTitle(), compareRows));
        }

        CompareHeader firstHeader = new CompareHeader();
        firstHeader.setName(firstElement.getTitle());
        firstHeader.setImgUrl(firstElement.getImgUrl());
        firstHeader.setUrl(firstElement.getUrl());
        CompareHeader secondHeader = new CompareHeader();
        secondHeader.setName(secondElement.getTitle());
        secondHeader.setImgUrl(secondElement.getImgUrl());
        secondHeader.setUrl(secondElement.getUrl());

        vitaminCompareResponse.setCompareHeaders(List.of(firstHeader, secondHeader));

        return vitaminCompareResponse;
    }

    private static List<CompareElement> compareElements(Content firstContent, Content secondContent) {
        CompareElement firstCompareElement = new CompareElement();
        firstCompareElement.setAmount(firstContent.getAmount());
        CompareElement secondCompareElement = new CompareElement();
        secondCompareElement.setAmount(secondContent.getAmount());

        BigDecimal firstAmount = ContentMatcher.findNumberFromString(firstContent.getAmount());
        BigDecimal secondAmount = ContentMatcher.findNumberFromString(secondContent.getAmount());

        int compareResult = firstAmount.compareTo(secondAmount);
        if (compareResult > 0) {
            firstCompareElement.setCompareEnum(CompareEnum.BETTER);
            secondCompareElement.setCompareEnum(CompareEnum.WORSE);
        } else if (compareResult < 0) {
            firstCompareElement.setCompareEnum(CompareEnum.WORSE);
            secondCompareElement.setCompareEnum(CompareEnum.BETTER);
        } else {
            firstCompareElement.setCompareEnum(CompareEnum.EQUAL);
            secondCompareElement.setCompareEnum(CompareEnum.EQUAL);
        }

        return List.of(firstCompareElement, secondCompareElement);
    }

    private static Content getContentWithNullControl(Map<String, Content> contentMap, String name) {
        Content firstContent = contentMap.get(name);
        if (firstContent == null) {
            firstContent = new Content();
            firstContent.setName(name);
            firstContent.setAmount(DEFAULT_AMOUNT);
        }

        return firstContent;
    }
}
