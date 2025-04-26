package org.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "store_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreProduct {
    @Getter
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(
                    name = "store",
                    column = @Column(name = "store_id", nullable = false)
            ),
            @AttributeOverride(
                    name = "product",
                    column = @Column(name = "product_id", nullable = false)
            )
    })
    private StoreProductId id;


    @MapsId("store")
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;


    @MapsId("product")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int stock;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // product listed price for the current store

}
