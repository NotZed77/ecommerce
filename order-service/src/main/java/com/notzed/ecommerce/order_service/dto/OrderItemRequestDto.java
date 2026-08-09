package com.notzed.ecommerce.order_service.dto;

import lombok.Data;

@Data
public class OrderItemRequestDto {
    private Long id;
    private Long productId;
    private Integer quantity;
}
