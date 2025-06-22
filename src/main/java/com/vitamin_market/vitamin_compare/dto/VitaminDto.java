package com.vitamin_market.vitamin_compare.dto;

import com.vitamin_market.vitamin_compare.entity.Content;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VitaminDto {

    private String id;
    private List<Content> vitaminContent;
    private List<Content> mineralContent;
    private String url;
    private String title;
    private String imgUrl;
}
