package com.ufps.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.ufps.springboot.webflux.app.models.dao.ProductoDao;
import com.ufps.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao productoDao;
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		mongoTemplate.dropCollection("productos").subscribe();
		
		Flux.just( new Producto("TV SONY", 3445.6),
				   new Producto("PC Acer", 8556.4),
				   new Producto("Mouse Genius", 6535.6),
				   new Producto("SSD Kinsgton", 97265.6),
				   new Producto("Ram Crucial", 98645.6)
				
				)
		.flatMap(producto -> {
			producto.setCreateAt(new Date());
			return productoDao.save(producto);
			
					})
		.subscribe(producto -> log.info("insert: " + producto.getId().concat(" - ").concat(producto.getNombre()))); 
		
	}

}
