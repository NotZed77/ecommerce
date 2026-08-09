package com.notzed.ecommerce.inventory_service.dto;

import lombok.Data;

@Data
public class OrderItemRequestDto {
    private Long productId;
    private Integer quantity;
}
