package com.tg.product.db;

/**
 * MongoDB document representing a Product object
 *
 */
public class MongoDBProductDocument extends AbstractMongoDBDocument {


	private String name;
	private String currencyID;
	
	public String getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(String currencyID) {
		this.currencyID = currencyID;
	}

	public MongoDBProductDocument(String id, String name, String currencyID) {
		super(id);
		//this.id = id;
		this.name = name;
		this.currencyID = currencyID;
	}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "," + "currencyID=" +currencyID+"]";
	}
}


