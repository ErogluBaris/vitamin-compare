package com.vitamin_market.vitamin_compare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CompareRow {

    private String contentName;
    private List<CompareElement> elements;
}
