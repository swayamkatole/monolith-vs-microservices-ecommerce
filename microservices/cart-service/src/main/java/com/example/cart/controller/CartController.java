package com.example.cart.controller;

import com.example.cart.model.CartItem;
import com.example.cart.repository.CartItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartItemRepository cartItemRepository;

    public CartController(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping("/{sessionId}")
    public List<CartItem> getCart(@PathVariable String sessionId) {
        return cartItemRepository.findBySessionId(sessionId);
    }

    @PostMapping("/{sessionId}/items")
    public CartItem addToCart(@PathVariable String sessionId, @RequestBody CartItemRequest request) {
        CartItem item = new CartItem();
        item.setSessionId(sessionId);
        item.setProductId(request.productId());
        item.setQuantity(request.quantity());
        item.setPrice(request.price());
        return cartItemRepository.save(item);
    }

    @DeleteMapping("/{sessionId}")
    public void clearCart(@PathVariable String sessionId) {
        cartItemRepository.deleteBySessionId(sessionId);
    }

    public record CartItemRequest(Long productId, Integer quantity, java.math.BigDecimal price) {}
}
