package com.vitamin_market.vitamin_compare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContentTypeEnum {
    VITAMIN_CONTENT("Vitamin İçeriği"),
    MINERAL_CONTENT("Mineral içeriği"),
    OTHER_CONTENT("Diğer İçerik");

    private final String title;
}
