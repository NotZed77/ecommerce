package com.notzed.ecommerce.order_service.client;

import com.ecommerce.shipping_service.dto.ShippingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "shipping-service", path="/shippings")
public interface ShippingOpenFeignClient {

    @PostMapping("/{shippingId}/confirmShipping")
    ShippingDto confirmedShipping(@PathVariable String shippingId);

}
