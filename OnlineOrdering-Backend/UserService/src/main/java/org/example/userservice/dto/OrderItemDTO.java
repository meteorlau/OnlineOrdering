package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private String storeName;
    private String storeLocation;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
}
