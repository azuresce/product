package com.tg.product.rest;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Object representing the aggregation of a product and its currency 
 */
@JsonPropertyOrder({ "id", "name", "currency_price" })
public class JsonProduct {
	
	String Id;
	String Name;
	private Map<String, String> currency_price;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	
    public Map<String, String> getCurrency_price() {
		return currency_price;
	}

	public void setCurrency_price(Map<String, String> currency_price) {
		this.currency_price = currency_price;
	}

	@Override
	public String toString() {
		return "JsonProduct [Id=" + Id + ", Name=" + Name + ", currency_price=" + currency_price + "]";
	}
	
}

