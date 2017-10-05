package com.tg.product.db;

/**
 * MongoDB document representing a Currency object
 *
 */
public class MongoDBCurrencyDocument extends AbstractMongoDBDocument {
	
	private String currency_code;
	private double currency_value;
	
	public MongoDBCurrencyDocument(String id, String currency_code, double currency_value) {
		super(id);
		this.currency_code = currency_code;
		this.currency_value = currency_value;
	}
	
	public String getCurrency_code() {
		return currency_code;
	}
	
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public double getCurrency_value() {
		return currency_value;
	}
	public void setCurrency_value(double currency_value) {
		this.currency_value = currency_value;
	}
	
	@Override
	public String toString() {
		return "Currency [currency_code=" + currency_code + ", currency_value=" + currency_value + "]";
	}
}
