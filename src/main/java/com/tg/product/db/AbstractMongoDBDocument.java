package com.tg.product.db;

/**
 * Abstract class representing a MongoDB document 
 * 
 */
public abstract class AbstractMongoDBDocument {
	
	// All documents need an id
	protected String id;

	public AbstractMongoDBDocument(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}