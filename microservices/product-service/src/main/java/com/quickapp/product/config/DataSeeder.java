package com.quickapp.product.config;

import com.quickapp.product.model.Product;
import com.quickapp.product.model.ProductCategory;
import com.quickapp.product.repository.ProductCategoryRepository;
import com.quickapp.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    public DataSeeder(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            logger.info("Seeding product data");

            ProductCategory noneCat = new ProductCategory();
            noneCat.setName("None");
            noneCat.setDescription("Default category. Products that have not been assigned a category");
            noneCat = categoryRepository.save(noneCat);

            Product p1 = new Product();
            p1.setName("BMW M6");
            p1.setDescription("Yet another masterpiece from the world's best car manufacturer");
            p1.setBuyingPrice(new BigDecimal("109775"));
            p1.setSellingPrice(new BigDecimal("114234"));
            p1.setUnitsInStock(12);
            p1.setIsActive(true);
            p1.setProductCategory(noneCat);
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setName("Nissan Patrol");
            p2.setDescription("A true man's choice");
            p2.setBuyingPrice(new BigDecimal("78990"));
            p2.setSellingPrice(new BigDecimal("86990"));
            p2.setUnitsInStock(4);
            p2.setIsActive(true);
            p2.setProductCategory(noneCat);
            productRepository.save(p2);

            // Additional seed data (Task 8)
            seedAdditionalData();

            logger.info("Product data seeded successfully");
        }
    }

    private void seedAdditionalData() {
        ProductCategory electronics = new ProductCategory();
        electronics.setName("Electronics");
        electronics.setDescription("Electronic devices and gadgets");
        electronics = categoryRepository.save(electronics);

        ProductCategory clothing = new ProductCategory();
        clothing.setName("Clothing");
        clothing.setDescription("Apparel and fashion items");
        clothing = categoryRepository.save(clothing);

        ProductCategory food = new ProductCategory();
        food.setName("Food");
        food.setDescription("Food and beverages");
        food = categoryRepository.save(food);

        ProductCategory books = new ProductCategory();
        books.setName("Books");
        books.setDescription("Books and publications");
        books = categoryRepository.save(books);

        ProductCategory sports = new ProductCategory();
        sports.setName("Sports");
        sports.setDescription("Sports equipment and accessories");
        sports = categoryRepository.save(sports);

        Object[][] products = {
            {"iPhone 15 Pro", "Latest Apple smartphone", new BigDecimal("999"), new BigDecimal("1199"), 25, electronics},
            {"Samsung Galaxy S24", "Premium Android phone", new BigDecimal("849"), new BigDecimal("999"), 30, electronics},
            {"Nike Air Max", "Classic running shoes", new BigDecimal("89"), new BigDecimal("129"), 100, clothing},
            {"Levi's 501 Jeans", "Original fit jeans", new BigDecimal("45"), new BigDecimal("69"), 200, clothing},
            {"Organic Coffee Beans", "Premium Arabica beans", new BigDecimal("12"), new BigDecimal("24"), 500, food},
            {"Dark Chocolate Bar", "72% cocoa premium chocolate", new BigDecimal("3"), new BigDecimal("6"), 1000, food},
            {"Clean Code", "Robert C. Martin's software craftsmanship guide", new BigDecimal("25"), new BigDecimal("45"), 50, books},
            {"Design Patterns", "Gang of Four classic", new BigDecimal("30"), new BigDecimal("55"), 40, books},
            {"Yoga Mat", "Premium non-slip exercise mat", new BigDecimal("15"), new BigDecimal("35"), 150, sports},
            {"Basketball", "Official size game ball", new BigDecimal("20"), new BigDecimal("39"), 80, sports}
        };

        for (Object[] data : products) {
            Product p = new Product();
            p.setName((String) data[0]);
            p.setDescription((String) data[1]);
            p.setBuyingPrice((BigDecimal) data[2]);
            p.setSellingPrice((BigDecimal) data[3]);
            p.setUnitsInStock((int) data[4]);
            p.setIsActive(true);
            p.setProductCategory((ProductCategory) data[5]);
            productRepository.save(p);
        }
    }
}
