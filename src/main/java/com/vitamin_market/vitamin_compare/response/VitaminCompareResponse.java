package com.vitamin_market.vitamin_compare.response;


import com.vitamin_market.vitamin_compare.domain.CompareHeader;
import com.vitamin_market.vitamin_compare.domain.ContentCompare;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VitaminCompareResponse {

    private List<CompareHeader> compareHeaders;
    private List<ContentCompare> compareResults;
}
