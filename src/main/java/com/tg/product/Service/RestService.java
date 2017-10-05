package com.tg.product.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tg.product.db.MongoDBCurrencyDocument;
import com.tg.product.db.AbstractMongoDBDocument;
import com.tg.product.db.MongoDBProductDocument;
import com.tg.product.db.InterfaceMongoDBRepository;
import com.tg.product.rest.JsonProduct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Rest service. Used to get product information or update data
 * information 
 *
 */
@Component
public class RestService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	InterfaceMongoDBRepository mongoDBrepository;
	
	private final String currencyCode ="currency_code";
	private final String value = "value";
	
	/**
	 * Aggregates the product data that the product id represents
	 * @param productId Product id that you want data from
	 * @return JsonProduct a json object representing the data for the product 
	 * @throws ResourceNotFoundException
	 */
	public JsonProduct getProductInformation(String productId) throws ResourceNotFoundException {

		logger.debug("Entering getProductInformation with product id : " + productId);

		// Check to make sure the product ID is valid
		if(productId == null || productId.length() <= 1) {
			logger.error("Product Id is incorrect : " + productId);
			throw new IllegalArgumentException();
		}
		
		// Look for the product in the mongodb
		AbstractMongoDBDocument productObject = mongoDBrepository.findById(productId);
		MongoDBProductDocument product = null;
		
		if( productObject != null && productObject instanceof MongoDBProductDocument) {
			logger.debug("Product found for Id : " + productId);			
			product = (MongoDBProductDocument)productObject;
		}
		else {
			logger.error("No product found for Id : " + productId);			
			throw new ResourceNotFoundException("No product found for Id: "+ productId);
		}
		
		// Get the currency object that is associated with the product
		String currentId = product.getCurrencyID();
		
		// Assume that a product must always have a currency object related to it. If not 
		AbstractMongoDBDocument currencyObject = null;
		MongoDBCurrencyDocument currency = null;
		if(currentId != null ){
			currencyObject = mongoDBrepository.findById(currentId);
			if(currencyObject != null && currencyObject instanceof MongoDBCurrencyDocument) {
				currency = (MongoDBCurrencyDocument) currencyObject;
			} else {
				logger.error("No product found for Id : " + productId);			
				throw new ResourceNotFoundException("There is no currency for this product: " +productId );
			}
		} else {
			logger.error("No product found for Id : " + productId);			
			throw new ResourceNotFoundException("There is no currency ID for this product: " +productId );
		}

		return constructJsonObject(product, currency);		
	}

	/**
	 * Updates the Product and Currency objects that the product is made up of
	 * @param jsonProduct The information used to update the product
	 * @throws ResourceNotFoundException
	 */
	public void updateProductInformation(JsonProduct jsonProduct) throws ResourceNotFoundException, IllegalArgumentException {
		
		if(jsonProduct == null ) {
			throw new IllegalArgumentException("The JsonProduct is null");
		}
		
		// Validate the JsonProduct
        validateJsonProduct(jsonProduct);
		
		logger.debug("Entering updateProductInformation()");
		logger.debug("JsonProduct : " +jsonProduct.toString());
		AbstractMongoDBDocument mongoDBDocument = mongoDBrepository.findById(jsonProduct.getId());
		if(mongoDBDocument == null) throw new ResourceNotFoundException("The product does not exist");
		MongoDBProductDocument product = null;
		if(mongoDBDocument != null && mongoDBDocument instanceof MongoDBProductDocument) {
			product = (MongoDBProductDocument)mongoDBDocument;
			AbstractMongoDBDocument mongoDBDocument2 = mongoDBrepository.findById(product.getCurrencyID());
			if(mongoDBDocument2 == null) {
				logger.error("No currency object found for this product");
				throw new ResourceNotFoundException("No currency object found for this product.");
			}
			MongoDBCurrencyDocument currency = null;
			if( mongoDBDocument != null && mongoDBDocument2 instanceof MongoDBCurrencyDocument) {
				logger.debug("Found currency object and updating it.");				
				currency = (MongoDBCurrencyDocument) mongoDBDocument2;
				Map<String,String> currencyMap = jsonProduct.getCurrency_price();
				currency.setCurrency_code(currencyMap.get("currency_code"));
				currency.setCurrency_value(Double.parseDouble(currencyMap.get("value")));
				// There must be a better way to update a document 
				mongoDBrepository.delete(currency);
				logger.debug("Saving new currency");
				mongoDBrepository.save(currency);
			}  else {
				logger.error("No currency found for Id : " + jsonProduct.getId());			
				throw new ResourceNotFoundException("No currency found for Id : " + jsonProduct.getId() );
			}
		} else {
			logger.error("No product found for Id : " + jsonProduct.getId());			
			throw new ResourceNotFoundException("No product found for Id : " + jsonProduct.getId() );
		}
		logger.debug("Leaving updateProductInformation().");

	}
	
	/*
	 * Validate the jsonProduct making sure everything is set
	 */
	private void validateJsonProduct(JsonProduct jsonProduct) {
     
		if(jsonProduct.getCurrency_price() == null ||
		   jsonProduct.getId() ==null ||
		   jsonProduct.getName() == null ) {
			logger.error("The jsonProduct passed in is malformed");
			throw new IllegalArgumentException("The jsonProduct passed in is malformed.");		
		}
		
		Map<String, String> currencyMap = jsonProduct.getCurrency_price();
		
		if(currencyMap.get("value") == null) {
			logger.error("The value for the currency is not set");
			throw new IllegalArgumentException("The value for the currency is not set");
		}
		
		if(currencyMap.get("currency_code") == null) {
			logger.error("The currency code for the currency is not set");
			throw new IllegalArgumentException("The currency code for the currency is not set");
		}
	}

	/**
	 * Construct the Json object representing the product and it currency
	 * @param product The product object
	 * @param curency The currency object for the product
	 * @return JsonProduct that represents the product and its currency
	 */
	private JsonProduct constructJsonObject(MongoDBProductDocument product, MongoDBCurrencyDocument curency) {
		logger.debug("Entering constructJsonObject().");
		JsonProduct jsonProduct = new JsonProduct();
		
		// Add the Product information to the jasonproduct
		jsonProduct.setId(product.getId());
		jsonProduct.setName(product.getName());
		
		// Create a map to hold the currency data
		Map<String,String> currencyMap = new HashMap<String,String>();
		currencyMap.put(currencyCode, curency.getCurrency_code());
		currencyMap.put(value, Double.toString(curency.getCurrency_value()));
		// Add the currency data to the jsonproduct object
		jsonProduct.setCurrency_price(currencyMap);
		logger.debug("Constructed JsonProduct : " + jsonProduct.toString());
		logger.debug("Leaving constructJsonObject().");
		return jsonProduct;
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public ResourceNotFoundException() {super();}
	    public ResourceNotFoundException(String message, Throwable cause) {super(message, cause);}
	    public ResourceNotFoundException(String message) {super(message);}
        public ResourceNotFoundException(Throwable cause) {super(cause);}
	}
}
