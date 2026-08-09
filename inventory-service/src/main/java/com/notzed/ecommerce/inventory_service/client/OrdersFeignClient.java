package com.notzed.ecommerce.inventory_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service", path="/order")
public interface OrdersFeignClient {

    @GetMapping("/core/helloOrders")
    String helloOrders();

}
