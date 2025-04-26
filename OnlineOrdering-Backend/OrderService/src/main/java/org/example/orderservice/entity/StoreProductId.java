package org.example.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

// (Composite Key Class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductId implements Serializable {
    private Long store;
    private Long product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreProductId)) return false;
        StoreProductId that = (StoreProductId) o;
        return Objects.equals(store, that.store) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, product);
    }
}
