package com.vitamin_market.vitamin_compare.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@Document(collection = "vitamin")
public class VitaminDocument {

    @Id
    private String id;
    private List<Content> vitaminContent;
    private List<Content> mineralContent;
    private List<Content> otherContent;
    private String url;
    private String title;
    @Field("img_url")
    private String imgUrl;
}
