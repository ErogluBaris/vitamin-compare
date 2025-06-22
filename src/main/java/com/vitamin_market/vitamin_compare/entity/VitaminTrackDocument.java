package com.vitamin_market.vitamin_compare.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "vitaminTrack")
public class VitaminTrackDocument {

    @Id
    private String id;
    private Long visitCount;
}
