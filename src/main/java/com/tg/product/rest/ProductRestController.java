package com.tg.product.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tg.product.Service.RestService;
/**
 * Rest Controller to define the Rest UIR end points
 */
@RestController
public class ProductRestController {
	
	@Autowired
	RestService aggregator;

	@RequestMapping(value = "/products/{someID}", method = RequestMethod.GET, produces = "application/json")
	public JsonProduct getProduct(@PathVariable(value="someID") String id) {
       JsonProduct reply = aggregator.getProductInformation(id);
	   return reply;
	}
	
	@RequestMapping(value = "/products/{someID}", method = RequestMethod.PUT, consumes = "application/json" )
	public ResponseEntity<JsonProduct>  updateProduct(@PathVariable(value="someID") String id,@RequestBody JsonProduct productInformation) {
        aggregator.updateProductInformation(productInformation);
	    return new ResponseEntity<JsonProduct>(productInformation, HttpStatus.OK);
	}
}
