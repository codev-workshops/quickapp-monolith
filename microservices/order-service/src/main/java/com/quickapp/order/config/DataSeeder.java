package com.quickapp.order.config;

import com.quickapp.order.model.Order;
import com.quickapp.order.model.OrderDetail;
import com.quickapp.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private final OrderRepository orderRepository;

    public DataSeeder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        if (orderRepository.count() == 0) {
            logger.info("Seeding order data");

            Order order1 = new Order();
            order1.setDiscount(new BigDecimal("500"));
            order1.setCashierId("admin");
            order1.setCustomerId(1);

            OrderDetail d1 = new OrderDetail();
            d1.setUnitPrice(new BigDecimal("114234"));
            d1.setQuantity(1);
            d1.setProductId(1);
            d1.setOrder(order1);
            order1.getOrderDetails().add(d1);

            OrderDetail d2 = new OrderDetail();
            d2.setUnitPrice(new BigDecimal("86990"));
            d2.setQuantity(1);
            d2.setProductId(2);
            d2.setOrder(order1);
            order1.getOrderDetails().add(d2);

            orderRepository.save(order1);

            Order order2 = new Order();
            order2.setCashierId("admin");
            order2.setCustomerId(2);

            OrderDetail d3 = new OrderDetail();
            d3.setUnitPrice(new BigDecimal("86990"));
            d3.setQuantity(1);
            d3.setProductId(2);
            d3.setOrder(order2);
            order2.getOrderDetails().add(d3);

            orderRepository.save(order2);

            // Additional seed data (Task 8)
            seedAdditionalOrders();

            logger.info("Order data seeded successfully");
        }
    }

    private void seedAdditionalOrders() {
        Random random = new Random(42);
        int[] customerIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] productIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        BigDecimal[] prices = {
            new BigDecimal("114234"), new BigDecimal("86990"), new BigDecimal("1199"),
            new BigDecimal("999"), new BigDecimal("129"), new BigDecimal("69"),
            new BigDecimal("24"), new BigDecimal("6"), new BigDecimal("45"), new BigDecimal("35")
        };

        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setCashierId("admin");
            order.setCustomerId(customerIds[i % customerIds.length]);
            order.setDiscount(new BigDecimal(random.nextInt(100)));

            int numDetails = 1 + random.nextInt(3);
            for (int j = 0; j < numDetails; j++) {
                OrderDetail detail = new OrderDetail();
                int pidx = random.nextInt(productIds.length);
                detail.setProductId(productIds[pidx]);
                detail.setUnitPrice(prices[pidx]);
                detail.setQuantity(1 + random.nextInt(5));
                detail.setOrder(order);
                order.getOrderDetails().add(detail);
            }

            orderRepository.save(order);
        }
    }
}
