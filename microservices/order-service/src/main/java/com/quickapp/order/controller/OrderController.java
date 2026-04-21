package com.quickapp.order.controller;

import com.quickapp.order.dto.OrderDTO;
import com.quickapp.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(HttpServletRequest request) {
        return ResponseEntity.ok(orderService.getAllOrders(extractToken(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int id, HttpServletRequest request) {
        return ResponseEntity.ok(orderService.getOrderById(id, extractToken(request)));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto, Authentication auth,
                                                 HttpServletRequest request) {
        return ResponseEntity.ok(orderService.createOrder(dto, auth.getName(), extractToken(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable int id, @RequestBody OrderDTO dto,
                                                 Authentication auth, HttpServletRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, dto, auth.getName(), extractToken(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
