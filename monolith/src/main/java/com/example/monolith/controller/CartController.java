package com.example.monolith.controller;

import com.example.monolith.model.CartItem;
import com.example.monolith.repository.CartItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartItemRepository cartItemRepository;

    public CartController(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @PostMapping("/{userId}/items")
    public CartItem addToCart(@PathVariable Long userId, @RequestBody CartItem item) {
        item.setUserId(userId);
        return cartItemRepository.save(item);
    }
}
