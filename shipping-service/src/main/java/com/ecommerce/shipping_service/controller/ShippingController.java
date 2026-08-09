package com.ecommerce.shipping_service.controller;

import com.ecommerce.shipping_service.dto.ShippingDto;
import com.ecommerce.shipping_service.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shippings")
@RequiredArgsConstructor
@Slf4j
public class ShippingController {

    private final ShippingService shippingService;

    @PutMapping("/{shippingId}")
    public ResponseEntity<ShippingDto> confirmShipping(@RequestBody Long shippingId){
        ShippingDto shippingDto = shippingService.confirmedShipping(shippingId);
        return ResponseEntity.ok(shippingDto);
    }
}
