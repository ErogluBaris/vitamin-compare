package com.vitamin_market.vitamin_compare.controller;

import com.vitamin_market.vitamin_compare.dto.VitaminDropdownDto;
import com.vitamin_market.vitamin_compare.dto.VitaminDto;
import com.vitamin_market.vitamin_compare.service.VitaminService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/v1/vitamins")
@RestController
public class VitaminController {

    private final VitaminService vitaminService;

    @GetMapping("/{id}")
    public VitaminDto getById(@PathVariable String id) {
        return vitaminService.getById(id);
    }

    @GetMapping("/get-dropdown")
    public List<VitaminDropdownDto> getDropdown(@RequestParam String titleLikeText) {
        return vitaminService.getDropdown(titleLikeText);
    }

    @GetMapping("/get-popular")
    public List<VitaminDto> getPopular() {
        return vitaminService.getPopular();
    }
}
