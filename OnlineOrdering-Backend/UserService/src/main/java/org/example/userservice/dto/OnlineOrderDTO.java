package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineOrderDTO {
    private UUID id;
    private String username;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
}
