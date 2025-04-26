package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(
                    name = "onlineOrder",
                    column = @Column(name = "order_id", nullable = false)
            ),
            @AttributeOverride(
                    name = "store",
                    column = @Column(name = "store_id", nullable = false)
            ),
            @AttributeOverride(
                    name = "product",
                    column = @Column(name = "product_id", nullable = false)
            )
    })
    private OrderItemId id;

    @MapsId("onlineOrder")
    @ManyToOne(fetch = FetchType.LAZY) // loaded on-demand, only when accessed
    @JoinColumn(name = "order_id", nullable = false)
    private OnlineOrder onlineOrder;

    @MapsId("store")
    @ManyToOne(fetch = FetchType.LAZY) // loaded on-demand, only when accessed
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @MapsId("product")
    @ManyToOne(fetch = FetchType.LAZY) // loaded on-demand, only when accessed
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // product price when onlineOrder is placed

    // DO NOT include lazy collections here
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
