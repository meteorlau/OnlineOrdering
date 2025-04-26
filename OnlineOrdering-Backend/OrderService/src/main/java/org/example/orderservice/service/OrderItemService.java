package org.example.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.orderservice.dto.OrderItemDTO;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.entity.OrderItemId;
import org.example.orderservice.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final StoreService storeService;
    private final ProductService productService;


    public OrderItemService(OrderItemRepository orderItemRepository, StoreService storeService, ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.storeService = storeService;
        this.productService = productService;
    }

    // ----- Create ----- (handled by OrderService)


    // ----- Read -----
    public OrderItem getOrderItemById(OrderItemId orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found: " + orderItemId));
    }

    // ----- Update -----

    // *CONSIDER UPDATING THE ORDER TOTAL PRICE
    @Transactional
    public void updateQuantity(OrderItemId orderItemId, int newQuantity) {
        OrderItem orderItem = getOrderItemById(orderItemId);

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        if (orderItem.getQuantity() == newQuantity) {
            throw new IllegalArgumentException("Quantity remains the same.");
        }

        int updated = orderItemRepository.updateQuantity(orderItem.getId(), newQuantity);
        if (updated == 0) {
            throw new IllegalStateException("Failed to update quantity.");
        }
    }

    @Transactional
    public void updateUnitPrice(OrderItemId orderItemId, BigDecimal newPrice) {
        OrderItem orderItem = getOrderItemById(orderItemId);

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        if (orderItem.getUnitPrice().compareTo(newPrice) == 0) {
            throw new IllegalArgumentException("Price remains the same.");
        }

        int updated = orderItemRepository.updateUnitPrice(orderItem.getId(), newPrice);
        if (updated == 0) {
            throw new IllegalStateException("Failed to update unit price.");
        }
    }

    // ----- DTO -----
    public OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setStoreName(orderItem.getStore().getName());
        dto.setStoreLocation(orderItem.getStore().getLocation());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        return dto;
    }

    public OrderItem createOrderItemFromDTO(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setStore(storeService.getActiveStoreByNameAndLocation(orderItemDTO.getStoreName(), orderItemDTO.getStoreLocation()));
        orderItem.setProduct(productService.getProductByName(orderItemDTO.getProductName()));
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setUnitPrice(orderItemDTO.getUnitPrice());
        return orderItem;
    }
}
