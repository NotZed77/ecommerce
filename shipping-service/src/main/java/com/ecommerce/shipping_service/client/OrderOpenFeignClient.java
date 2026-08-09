package com.ecommerce.shipping_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service", path="/order")
public interface OrderOpenFeignClient {
}
