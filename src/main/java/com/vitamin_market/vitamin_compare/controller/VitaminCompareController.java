package com.vitamin_market.vitamin_compare.controller;

import com.vitamin_market.vitamin_compare.request.VitaminCompareRequest;
import com.vitamin_market.vitamin_compare.response.VitaminCompareResponse;
import com.vitamin_market.vitamin_compare.service.VitaminCompareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/vitamin-compare")
@RestController
public class VitaminCompareController {

    private final VitaminCompareService vitaminCompareService;

    @PostMapping("/compare")
    public VitaminCompareResponse compare(@RequestBody VitaminCompareRequest request) {
        return vitaminCompareService.getVitaminCompareResponse(request);
    }
}
