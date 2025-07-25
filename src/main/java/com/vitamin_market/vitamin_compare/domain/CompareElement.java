package com.vitamin_market.vitamin_compare.domain;

import com.vitamin_market.vitamin_compare.enums.CompareEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompareElement {

    private String amount;
    private CompareEnum compareEnum;
}
