package com.tg.product;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import com.tg.product.Service.RestService;
import com.tg.product.db.InterfaceMongoDBRepository;
import com.tg.product.db.MongoDBCurrencyDocument;
import com.tg.product.db.MongoDBProductDocument;
import com.tg.product.rest.JsonProduct;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ProductApplication.class)
public class RestTests {
	
	@Autowired
	RestService restService;
	
	@Autowired
	InterfaceMongoDBRepository repository;

	@Autowired
	private TestRestTemplate restTemplate;	
	
	@Test
	public void noAuthTestRest() {		
		String body = this.restTemplate.getForObject("/products/15117729",String.class);
		assertThat(body, containsString("Full authentication is required to access this resource"));
	}
	
	@Test
	public void wrongUserTestRest() {		
		String body = this.restTemplate.withBasicAuth("admind", "target").getForObject("/products/15117729",String.class);
		assertThat(body, containsString("Bad credentials"));
	}
	
	@Test
	public void getProductRest() {		
		String body = this.restTemplate.withBasicAuth("admin", "target1c").getForObject("/products/15117729",String.class);
		assertThat(body, containsString("\"The Hitchhicker's Guild to the Universe (Blu-ray) (Widescreen)\""));
	}
	
	@Test
	public void noProductRest() {		
		String body = this.restTemplate.withBasicAuth("admin", "target1c").getForObject("/products/1517729",String.class);
		assertThat(body, containsString("No product found for Id: 1517729"));
	}
	
	@Test
	public void putProductRest() {	
	    String productId = "16752456";
		JsonProduct jsonProduct = restService.getProductInformation(productId);
		assertThat(jsonProduct, is(notNullValue()));
		assertThat(jsonProduct.getId(), is(productId));
		Map<String,String> currencyMap = jsonProduct.getCurrency_price();
		assertThat(currencyMap, is(notNullValue()));
		assertThat(currencyMap.get("value"), is("2.5"));
		currencyMap.replace("value", "200.89");
		
		this.restTemplate.withBasicAuth("admin", "target1c").put("/products/" + productId, jsonProduct, JsonProduct.class);
		
		MongoDBProductDocument product = (MongoDBProductDocument) repository.findById(productId);
		MongoDBCurrencyDocument currency = (MongoDBCurrencyDocument) repository.findById(product.getCurrencyID());
		assertThat(Double.toString(currency.getCurrency_value()), is("200.89"));
	}
}
