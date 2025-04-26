package org.example.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.menuservice.entity.Product;
import org.example.menuservice.entity.StoreProduct;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithStoresDTO {
    private Product product;
    private List<StoreProduct> availableStores;
}
