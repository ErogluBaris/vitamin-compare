package com.vitamin_market.vitamin_compare.service;

import com.vitamin_market.vitamin_compare.domain.*;
import com.vitamin_market.vitamin_compare.entity.Content;
import com.vitamin_market.vitamin_compare.entity.VitaminDocument;
import com.vitamin_market.vitamin_compare.regex.ContentMatcher;
import com.vitamin_market.vitamin_compare.request.VitaminCompareRequest;
import com.vitamin_market.vitamin_compare.response.VitaminCompareResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VitaminCompareService {

    private static final String DEFAULT_AMOUNT = "-";
    private static final String VITAMIN_CONTENT = "Vitamin İçeriği";
    private static final String MINERAL_CONTENT = "Mineral İçeriği";

    private final VitaminService vitaminService;

    public VitaminCompareResponse getVitaminCompareResponse(VitaminCompareRequest request) {
        Map<String, VitaminDocument> vitaminMap = vitaminService.getByIds(
                Set.of(request.getFirstId(), request.getSecondId()));

        VitaminDocument firstElement = vitaminMap.get(request.getFirstId());
        VitaminDocument secondElement = vitaminMap.get(request.getSecondId());

        List<CompareRow> vitaminContentCompareRows = compare(firstElement.getVitaminContent(),
                secondElement.getVitaminContent());

        List<CompareRow> mineralContentCompareRows = compare(firstElement.getMineralContent(),
                secondElement.getMineralContent());

        VitaminCompareResponse vitaminCompareResponse = new VitaminCompareResponse();
        vitaminCompareResponse.setCompareResults(List.of(new ContentCompare(VITAMIN_CONTENT, vitaminContentCompareRows),
                new ContentCompare(MINERAL_CONTENT, mineralContentCompareRows)));

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

    private List<CompareRow> compare(List<Content> firstContents, List<Content> secondContents) {
        if (CollectionUtils.isEmpty(firstContents)) {
            firstContents = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(secondContents)) {
            secondContents = new ArrayList<>();
        }

        Map<String, Content> firstVitaminContentMap = firstContents.stream()
                .collect(Collectors.toMap(Content::getName, Function.identity()));

        Map<String, Content> secondVitaminContentMap = secondContents.stream()
                .collect(Collectors.toMap(Content::getName, Function.identity()));

        Set<String> allVitaminContentKeys = new HashSet<>(firstVitaminContentMap.keySet());
        allVitaminContentKeys.addAll(secondVitaminContentMap.keySet());

        List<CompareRow> compareRows = new ArrayList<>();
        for (String key : allVitaminContentKeys) {
            Content firstContent = getContentWithNullControl(firstVitaminContentMap, key);
            Content secondContent = getContentWithNullControl(secondVitaminContentMap, key);

            List<CompareElement> compareElements = compareElements(firstContent, secondContent);
            compareRows.add(new CompareRow(key, compareElements));
        }

        return compareRows;
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
