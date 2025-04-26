package org.example.orderservice.repository;

import org.example.orderservice.entity.Enum.OrderStatus;
import org.example.orderservice.entity.OnlineOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OnlineOrder, UUID> {
    // Find orders above a certain total
    List<OnlineOrder> findByUser_Id(Long userId);

    // [user] find orders by status and username
    List<OnlineOrder> findByUser_IdAndStatus(Long userId, OrderStatus status);

    // [user] Find the most recent order by username
    Optional<OnlineOrder> findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    // [user] Find orders within a time interval by username
    List<OnlineOrder> findByUser_IdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);


    // [ADMIN] read sum of totalPrices of within a time interval for COMPLETED orders
    @Query("SELECT SUM(o.totalPrice) FROM OnlineOrder o WHERE o.status = 'COMPLETED' AND o.createdAt BETWEEN :start AND :end")
    BigDecimal sumCompletedOrderTotalPriceBetweenDates(@Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);

    // [ADMIN] Optional: Find orders above a certain total
    List<OnlineOrder> findByTotalPriceGreaterThan(BigDecimal totalPrice);

    // [ADMIN] find orders by status
    List<OnlineOrder> findByStatus(OrderStatus status);

    // [ADMIN] Optional: count orders by status (e.g. PENDING, COMPLETED)
    long countByStatus(OrderStatus status);

    // [ADMIN] find recent N orders (by creation time)
    List<OnlineOrder> findTop10ByOrderByCreatedAtDesc();

    // [ADMIN] Find orders within certain time interval
    List<OnlineOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // [ADMIN] Optional: count orders by time interval
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // [ADMIN] update OnlineOrder status
    @Modifying
    @Transactional
    @Query("UPDATE OnlineOrder o SET o.status = :status WHERE o.id = :orderId")
    int updateOrderStatus(@Param("orderId") UUID orderId, @Param("status") OrderStatus status);

    // [general] update total price
    @Modifying
    @Transactional
    @Query("UPDATE OnlineOrder o SET o.totalPrice = :totalPrice WHERE o.id = :orderId")
    int updateTotalPrice(@Param("orderId") UUID orderId, @Param("totalPrice") BigDecimal totalPrice);
}
