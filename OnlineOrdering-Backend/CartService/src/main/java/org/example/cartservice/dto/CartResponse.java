package org.example.cartservice.dto;

import java.util.List;

public class CartResponse {
    private String cartId;
    private List<CartItemResponse> items;

    public CartResponse() {}

    public CartResponse(String cartId, List<CartItemResponse> items) {
        this.cartId = cartId;
        this.items = items;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }
}
