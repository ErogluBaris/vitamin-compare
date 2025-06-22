package com.vitamin_market.vitamin_compare.repository;

import com.vitamin_market.vitamin_compare.entity.VitaminTrackDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitaminTrackRepository extends MongoRepository<VitaminTrackDocument, String> {

    @Query(value = "{}", sort = "{ 'viewCount': -1 }")
    List<VitaminTrackDocument> findAllOrderByViewCount(Pageable pageable);
}
