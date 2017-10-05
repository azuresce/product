package com.tg.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.tg.product.db.MongoDBCurrencyDocument;
import com.tg.product.db.MongoDBProductDocument;
import com.tg.product.db.InterfaceMongoDBRepository;

/**
 * Main App, no input needed.
 *
 */
@SpringBootApplication
@ComponentScan("com.tg")
public class ProductApplication implements CommandLineRunner {

    @Autowired
    InterfaceMongoDBRepository productRepository;
    
	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		// Make sure the repository is clean
		productRepository.deleteAll();
		
		// Add some Currency entries to the db
		productRepository.save(new MongoDBCurrencyDocument( "4323423423", "US", 12.5));
		productRepository.save(new MongoDBCurrencyDocument( "4323423424", "ZB", 16.56));
		productRepository.save(new MongoDBCurrencyDocument( "4323423425", "GB", 99.50));
		productRepository.save(new MongoDBCurrencyDocument( "4323423426", "US", 2.50));
		productRepository.save(new MongoDBCurrencyDocument( "4323423427", "US", 45.78));
        
		// Add some Product entries to the db
		productRepository.save(new MongoDBProductDocument("15117729", "The Hitchhicker's Guild to the Universe (Blu-ray) (Widescreen)","4323423423"));
		productRepository.save(new MongoDBProductDocument("16483589", "The Hitchhicker's Guild to the Universe(BOOK) (Paperback)","4323423424"));
		productRepository.save(new MongoDBProductDocument("16696652", "HG2G (DVD) (HD))","4323423425"));
		productRepository.save(new MongoDBProductDocument("16752456", "The Restaurant at the End of the Universe (Book) (HardCover) (Colectors Adition)", "4323423426"));
		productRepository.save(new MongoDBProductDocument("15643793", "So Long, and Thanks for All the Fish(DVD)", "4323423427"));
	}
}
