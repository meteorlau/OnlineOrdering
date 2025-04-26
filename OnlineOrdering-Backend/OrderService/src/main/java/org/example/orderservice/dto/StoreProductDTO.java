package org.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductDTO {
    private Long storeId;
    private Long productId;
    private int stock;
    private BigDecimal unitPrice;
}
