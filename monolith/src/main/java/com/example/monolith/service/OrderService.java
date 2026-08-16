package com.example.monolith.service;

import com.example.monolith.dto.CreateOrderRequest;
import com.example.monolith.model.Order;
import com.example.monolith.model.Product;
import com.example.monolith.repository.CartItemRepository;
import com.example.monolith.repository.OrderRepository;
import com.example.monolith.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository,
                        CartItemRepository cartItemRepository,
                        OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderRequest.OrderItemInput item : request.getItems()) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("PAID");
        order.setTotalAmount(total);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        cartItemRepository.deleteByUserId(request.getUserId());

        return saved;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
