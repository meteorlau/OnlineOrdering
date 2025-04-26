package org.example.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.orderservice.dto.OnlineOrderDTO;
import org.example.orderservice.entity.*;
import org.example.orderservice.entity.Enum.OrderStatus;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final StoreProductService storeProductService;


    public OrderService(OrderRepository orderRepository, StoreProductService storeProductService) {
        this.orderRepository = orderRepository;
        this.storeProductService = storeProductService;
    }

    // ----- Create -----
    public boolean orderExists(UUID orderId) {
        return orderRepository.existsById(orderId);
    }

    @Transactional
    public OnlineOrder insertOrder(User user, List<OrderItem> orderItems) {
        // Step 1: Validate inputs before saving onlineOrder
        if (user == null || orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("User and onlineOrder items must be provided.");
        }

        // Step 2: Create and save empty onlineOrder to get an ID
        OnlineOrder onlineOrder = new OnlineOrder();
        onlineOrder.setUser(user);
        onlineOrder.setStatus(OrderStatus.PENDING);
        onlineOrder.setTotalPrice(BigDecimal.ZERO); // temporary
        onlineOrder = orderRepository.save(onlineOrder); // now we have onlineOrder.getId()

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItems) {
            Store store = orderItem.getStore();
            Product product = orderItem.getProduct();

            if (store == null || product == null) {
                throw new IllegalArgumentException("Each onlineOrder item must have a store and product.");
            }

            // retrieve the product details from store
            StoreProductId spId = new StoreProductId(store.getId(), product.getId());
            StoreProduct storeProduct = storeProductService.getStoreProductById(spId);

            int quantity = orderItem.getQuantity();
            BigDecimal unitPrice = orderItem.getUnitPrice();

            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Unit price must be non-negative.");
            }
            if (quantity > storeProduct.getStock()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }

            // Decrement stock
            storeProductService.updateStock(spId, storeProduct.getStock() - quantity);

            // Prepare OrderItem
            orderItem.setId(new OrderItemId(onlineOrder.getId(), store.getId(), product.getId()));
            orderItem.setOnlineOrder(onlineOrder);
            orderItem.setStore(store);
            orderItem.setProduct(product);

            totalPrice = totalPrice.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        onlineOrder.setOrderItems(orderItems);
        onlineOrder.setTotalPrice(totalPrice);

        return orderRepository.save(onlineOrder); // saves onlineOrder and cascades to onlineOrder items
    }

    // ----- Read -----

    // -- [general] --
    public OnlineOrder getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("OnlineOrder not found: " + orderId));
    }

    public List<OnlineOrder> findOrdersByUserId(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public List<OnlineOrder> findAllOrders() {
        return orderRepository.findAll();
    }

    // ----- Update -----
    private void updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        OnlineOrder onlineOrder = getOrderById(orderId);

        if (onlineOrder.getStatus() == newStatus) {
            throw new IllegalStateException("OnlineOrder is already " + newStatus.name().toLowerCase() + ".");
        }

        int updated = orderRepository.updateOrderStatus(onlineOrder.getId(), newStatus);
        if (updated == 0) {
            throw new IllegalStateException("Failed to update onlineOrder.");
        }
    }

    @Transactional
    public void markOrderAsCompleted(UUID orderId) {
        updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }

    @Transactional
    public void markOrderAsCancelled(UUID orderId) {
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Transactional
    public void updateOrderTotalPrice(UUID orderId, BigDecimal totalPrice) {
        OnlineOrder onlineOrder = getOrderById(orderId);

        if (onlineOrder.getStatus() == OrderStatus.CANCELLED || onlineOrder.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("OnlineOrder is already processed. Failed to change total price.");
        }

        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        int updated = orderRepository.updateTotalPrice(onlineOrder.getId(), totalPrice);
        if (updated == 0) {
            throw new IllegalStateException("Failed to update onlineOrder.");
        }
    }

    // ----- DTO -----
    public OnlineOrderDTO convertOnlineOrderToDTO(OnlineOrder onlineOrder) {
        OnlineOrderDTO dto = new OnlineOrderDTO(onlineOrder.getId(),
                                                onlineOrder.getUser().getUsername(),
                                                onlineOrder.getStatus().name(),
                                                onlineOrder.getTotalPrice(),
                                                onlineOrder.getCreatedAt());
        return dto;
    }
}
