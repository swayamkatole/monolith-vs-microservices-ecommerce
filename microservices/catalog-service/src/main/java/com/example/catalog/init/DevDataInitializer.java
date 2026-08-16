package com.example.catalog.init;

import com.example.catalog.model.Product;
import com.example.catalog.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DevDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DevDataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        Product p1 = new Product();
        p1.setName("Laptop");
        p1.setDescription("Good laptop");
        p1.setPrice(new BigDecimal("999.99"));
        p1.setStock(50);

        Product p2 = new Product();
        p2.setName("Mouse");
        p2.setDescription("Wireless mouse");
        p2.setPrice(new BigDecimal("29.99"));
        p2.setStock(200);

        Product p3 = new Product();
        p3.setName("Keyboard");
        p3.setDescription("Mechanical keyboard");
        p3.setPrice(new BigDecimal("79.99"));
        p3.setStock(150);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
    }
}
