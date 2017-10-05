package com.tg.product;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import com.tg.product.Service.RestService;
import com.tg.product.db.InterfaceMongoDBRepository;
import com.tg.product.db.MongoDBCurrencyDocument;
import com.tg.product.db.MongoDBProductDocument;
import com.tg.product.rest.JsonProduct;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ProductApplication.class)
public class APITests {
	
	@Autowired
	RestService restService;
	
	@Autowired
	InterfaceMongoDBRepository repository;

	@Test
	public void getProductAPI() {		
  
		JsonProduct jsonProduct = restService.getProductInformation("16752456");
		assertThat(jsonProduct, is(notNullValue()));
		assertThat(jsonProduct.getId(), is("16752456"));
		assertThat(jsonProduct.getName(), is("The Restaurant at the End of the Universe (Book) (HardCover) (Colectors Adition)"));
		
		Map<String,String> currency_price = jsonProduct.getCurrency_price();
		assertThat(currency_price.size(), is(2));
		assertThat(currency_price.get("value"), is("2.5"));
		assertThat(currency_price.get("currency_code"), is("US"));
				
	}
	
	@Test
	public void upDateProductAPI() {		

		JsonProduct jsonProduct = restService.getProductInformation("15117729");
		assertThat(jsonProduct, is(notNullValue()));
		assertThat(jsonProduct.getId(), is("15117729"));
		assertThat(jsonProduct.getName(), is("The Hitchhicker's Guild to the Universe (Blu-ray) (Widescreen)"));
		
		Map<String,String> currency_price = jsonProduct.getCurrency_price();
		assertThat(currency_price.size(), is(2));
		assertThat(currency_price.get("value"), is("12.5"));
		assertThat(currency_price.get("currency_code"), is("US"));
		currency_price.replace("value", "30000");
				
		restService.updateProductInformation(jsonProduct);
		
		MongoDBProductDocument product = (MongoDBProductDocument) repository.findById(jsonProduct.getId());
		MongoDBCurrencyDocument currency =  (MongoDBCurrencyDocument) repository.findById(product.getCurrencyID());
		
		assertThat(currency.getCurrency_value(), is(30000.0));
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void getProductAPIWithNull() {
	    restService.getProductInformation(null);
	}
	
	@Test(expected = RestService.ResourceNotFoundException.class)
	public void getProductAPIWithNonExtantProduct() {
	    restService.getProductInformation("345677");
	}
	
	@Test(expected = RestService.ResourceNotFoundException.class)
	public void getProductAPIWithNonExtantCurrency() {
		repository.save(new MongoDBProductDocument("151177999929", "No Currency Product","43234299993423"));
	    restService.getProductInformation("151177999929");
	}
	
	@Test(expected = RestService.ResourceNotFoundException.class)
	public void getProductAPIWithNonExtantCurrencyID() {
		repository.save(new MongoDBProductDocument("15114444477999929", "No Currency Product",null));
	    restService.getProductInformation("15114444477999929");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateProductAPIWithNull() {
	    restService.getProductInformation(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateProductAPIWithNonExtantCurrencyOnObject() {
		JsonProduct jsonProduct = restService.getProductInformation("15117729");
        jsonProduct.getCurrency_price().clear();
	    restService.updateProductInformation(jsonProduct);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateProductAPIWithNullId() {
		JsonProduct jsonProduct = restService.getProductInformation("15117729");
        jsonProduct.setId(null);;
	    restService.updateProductInformation(jsonProduct);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateProductAPIWithNullName() {
		JsonProduct jsonProduct = restService.getProductInformation("15117729");
        jsonProduct.setName(null);
	    restService.updateProductInformation(jsonProduct);
	}
	
}
