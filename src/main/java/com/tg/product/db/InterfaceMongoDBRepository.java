package com.tg.product.db;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Interface extending the MongoReposiory for access to the mongodb
 *
 */
public interface InterfaceMongoDBRepository  extends MongoRepository<AbstractMongoDBDocument, String>{
    public AbstractMongoDBDocument findById(String id);
}