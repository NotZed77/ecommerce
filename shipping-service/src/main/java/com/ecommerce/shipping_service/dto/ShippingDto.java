package com.ecommerce.shipping_service.dto;

import com.ecommerce.shipping_service.entity.ShippingStatus;
import lombok.Data;

@Data
public class ShippingDto {
    private Long id;
    private Long orderId;
    private ShippingStatus shippingStatus;
}
