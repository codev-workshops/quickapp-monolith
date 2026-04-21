package com.quickapp.order.service;

import com.quickapp.order.client.CustomerClient;
import com.quickapp.order.client.ProductClient;
import com.quickapp.order.dto.OrderDTO;
import com.quickapp.order.dto.OrderDetailDTO;
import com.quickapp.order.model.Order;
import com.quickapp.order.model.OrderDetail;
import com.quickapp.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public OrderService(OrderRepository orderRepository, CustomerClient customerClient, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.customerClient = customerClient;
        this.productClient = productClient;
    }

    public List<OrderDTO> getAllOrders(String token) {
        return orderRepository.findAll().stream()
                .map(order -> toDTO(order, token))
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(int id, String token) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return toDTO(order, token);
    }

    public OrderDTO createOrder(OrderDTO dto, String userId, String token) {
        Order order = new Order();
        order.setDiscount(dto.getDiscount());
        order.setComments(dto.getComments());
        order.setCashierId(dto.getCashierId() != null ? dto.getCashierId() : userId);
        order.setCustomerId(dto.getCustomerId());
        order.setCreatedBy(userId);
        order.setUpdatedBy(userId);

        if (dto.getOrderDetails() != null) {
            for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {
                OrderDetail detail = new OrderDetail();
                detail.setUnitPrice(detailDTO.getUnitPrice());
                detail.setQuantity(detailDTO.getQuantity());
                detail.setDiscount(detailDTO.getDiscount());
                detail.setProductId(detailDTO.getProductId());
                detail.setOrder(order);
                detail.setCreatedBy(userId);
                detail.setUpdatedBy(userId);
                order.getOrderDetails().add(detail);
            }
        }

        order = orderRepository.save(order);
        return toDTO(order, token);
    }

    public OrderDTO updateOrder(int id, OrderDTO dto, String userId, String token) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (dto.getDiscount() != null) order.setDiscount(dto.getDiscount());
        if (dto.getComments() != null) order.setComments(dto.getComments());
        if (dto.getCustomerId() > 0) order.setCustomerId(dto.getCustomerId());
        order.setUpdatedBy(userId);

        order = orderRepository.save(order);
        return toDTO(order, token);
    }

    public void deleteOrder(int id) {
        if (!orderRepository.existsById(id)) throw new RuntimeException("Order not found");
        orderRepository.deleteById(id);
    }

    private OrderDTO toDTO(Order order, String token) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setDiscount(order.getDiscount());
        dto.setComments(order.getComments());
        dto.setCashierId(order.getCashierId());
        dto.setCustomerId(order.getCustomerId());

        if (token != null) {
            dto.setCustomerName(customerClient.getCustomerName(order.getCustomerId(), token));
        }

        if (order.getOrderDetails() != null) {
            dto.setOrderDetails(order.getOrderDetails().stream()
                    .map(detail -> toDetailDTO(detail, token))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private OrderDetailDTO toDetailDTO(OrderDetail detail, String token) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(detail.getId());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setQuantity(detail.getQuantity());
        dto.setDiscount(detail.getDiscount());
        dto.setProductId(detail.getProductId());

        if (token != null) {
            dto.setProductName(productClient.getProductName(detail.getProductId(), token));
        }

        return dto;
    }
}
