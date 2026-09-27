package com.notzed.ecommerce.order_service.client;

import com.notzed.ecommerce.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service", path="/inventory", url = "${INVENTORY_SERVICE_URI:}")
public interface InventoryOpenFeignClient {

    @PutMapping("/products/reduce-stocks")
    Double reduceStocks(@RequestBody OrderRequestDto orderRequestDto);

    @PutMapping("/products/{productId}/restock")
    void restock(@PathVariable Long productId, @RequestParam Integer quantity);
}
