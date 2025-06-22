package com.vitamin_market.vitamin_compare.repository;

import com.vitamin_market.vitamin_compare.entity.VitaminDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitaminRepository extends MongoRepository<VitaminDocument, String> {

    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<VitaminDocument> findAllByTitleLikeIgnoreCase(String titleLikeText, Pageable pageable);
}
