package org.example.cartservice.controller;

import org.example.cartservice.dto.AddItemRequest;
import org.example.cartservice.dto.CartResponse;
import org.example.cartservice.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addItem(
            @PathVariable String cartId,
            @RequestBody AddItemRequest req
    ) {
        CartResponse updated = cartService.addItem(cartId, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable String cartId,
            @PathVariable Long itemId
    ) {
        CartResponse updated = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> viewCart(@PathVariable String cartId) {
        CartResponse cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable String cartId) {
        cartService.clearCart(cartId);
        // 204 No Content is conventional for successful delete
        return ResponseEntity.noContent().build();
    }
}
