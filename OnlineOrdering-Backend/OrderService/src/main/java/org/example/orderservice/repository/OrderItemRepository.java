package org.example.orderservice.repository;

import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    Optional<OrderItem> findById(OrderItemId orderItemId);

    List<OrderItem> findByOnlineOrder_Id(UUID orderId);

    // Optional: find items by product name (if applicable)
    List<OrderItem> findByProductNameContainingIgnoreCase(String keyword);

    List<OrderItem> findByStore_Id(Long storeId);

    List<OrderItem> findByProduct_Id(Long productId);

    // [ADMIN] read sum of total prices for COMPLETED orderItems with the specified store
    @Query("SELECT SUM(oi.unitPrice * oi.quantity) FROM OrderItem oi JOIN oi.onlineOrder o " +
            "WHERE oi.store.id = :storeId AND o.status = 'COMPLETED'")
    BigDecimal sumCompletedOrderTotalPriceByStore(@Param("storeId") Long storeId);

    // [ADMIN] read sum of total prices for COMPLETED orderItems with the specified product
    @Query("SELECT SUM(oi.unitPrice * oi.quantity) FROM OrderItem oi JOIN oi.onlineOrder o " +
            "WHERE oi.product.id = :productId AND o.status = 'COMPLETED'")
    BigDecimal sumCompletedOrderTotalPriceByProduct(@Param("productId") Long productId);

    // [ADMIN] read the sum of the prices (unitPrice * quantity) for OrderItems with the specified store and product names,
    // and ensure the associated OnlineOrder's status is 'COMPLETED'
    @Query("SELECT SUM(oi.unitPrice * oi.quantity) FROM OrderItem oi JOIN oi.onlineOrder o " +
            "WHERE oi.store.id = :storeId AND oi.product.id = :productId AND o.status = 'COMPLETED'")
    BigDecimal sumCompletedOrderTotalPriceByStoreAndProduct(@Param("storeId") Long storeId,
                                                            @Param("productId") Long productId);


    // [ADMIN] future feature: Find the product that earns the highest income among all product within a store



    // [User] Update quantity for a specific order-product
    @Modifying
    @Transactional
    @Query("UPDATE OrderItem oi SET oi.quantity = :newQuantity WHERE oi.id = :orderItemId")
    int updateQuantity(@Param("orderItemId") OrderItemId orderItemId, @Param("newQuantity") int newQuantity);

    // [User] Update unit price for a specific order-product
    @Modifying
    @Transactional
    @Query("UPDATE OrderItem oi SET oi.unitPrice = :newPrice WHERE oi.id = :orderItemId")
    int updateUnitPrice(@Param("orderItemId") OrderItemId orderItemId, @Param("newPrice") BigDecimal newPrice);
}
