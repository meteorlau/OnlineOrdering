package org.example.cartservice.service;

import org.example.cartservice.dto.AddItemRequest;
import org.example.cartservice.dto.CartItemResponse;
import org.example.cartservice.dto.CartResponse;
import org.example.cartservice.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartService {

    private static final String KEY_PREFIX = "cart:";

    private final HashOperations<String, String, Integer> hashOps;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate;

    @Value("${menu.service.url}")
    private String menuServiceUrl;

    @Autowired
    public ShoppingCartService(RedisTemplate<String, Object> redisTemplate,
                               RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }

    public CartResponse addItem(String cartId, AddItemRequest req) {
        String key = KEY_PREFIX + cartId;

        MenuItem menu = fetchMenuItem(req.getItemId());
        if (menu == null) {
            throw new IllegalArgumentException("Item not found: " + req.getItemId());
        }

        Integer current = hashOps.get(key, req.getItemId().toString());
        int newQty = (current == null ? 0 : current) + req.getQuantity();
        if (newQty > menu.getStock()) {
            throw new IllegalArgumentException("Not enough stock for item " + req.getItemId()
                    + " (max " + menu.getStock() + ")");
        }

        hashOps.put(key, req.getItemId().toString(), newQty);
        return getCart(cartId);
    }

    public CartResponse removeItem(String cartId, Long itemId) {
        String key = KEY_PREFIX + cartId;
        hashOps.delete(key, itemId.toString());
        return getCart(cartId);
    }

    public CartResponse getCart(String cartId) {
        String key = KEY_PREFIX + cartId;
        Map<String, Integer> entries = hashOps.entries(key);
        List<CartItemResponse> items = new ArrayList<>();

        for (Map.Entry<String, Integer> e : entries.entrySet()) {
            Long id = Long.valueOf(e.getKey());
            MenuItem menu = fetchMenuItem(id);
            if (menu != null) {
                items.add(new CartItemResponse(
                        id,
                        menu.getName(),
                        e.getValue(),
                        menu.getPrice(),
                        menu.getStoreName(),
                        menu.getStoreLocation()
                ));
            }
        }

        return new CartResponse(cartId, items);
    }

    public void clearCart(String cartId) {
        redisTemplate.delete(KEY_PREFIX + cartId);
    }

    private MenuItem fetchMenuItem(Long id) {
        try {
            String url = menuServiceUrl + "/products/" + id;
            MenuItem item = restTemplate.getForObject(url, MenuItem.class);
            return item;
        } catch (RestClientException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
