package com.vitamin_market.vitamin_compare.service;

import com.vitamin_market.vitamin_compare.entity.VitaminTrackDocument;
import com.vitamin_market.vitamin_compare.mapper.VitaminTrackMapper;
import com.vitamin_market.vitamin_compare.repository.VitaminTrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VitaminTrackService {

    private final MongoTemplate mongoTemplate;
    private final VitaminTrackRepository repository;
    private final VitaminTrackMapper mapper;

    public void incrementTrackCount(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("viewCount", 1);
        mongoTemplate.upsert(query, update, VitaminTrackDocument.class);
    }

    public List<VitaminTrackDocument> findPopular() {
        Pageable pageable = PageRequest.of(0, 10);
        return repository.findAllOrderByViewCount(pageable);
    }
}
