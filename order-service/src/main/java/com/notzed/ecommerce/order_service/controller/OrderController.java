package com.notzed.ecommerce.order_service.controller;

import com.notzed.ecommerce.order_service.client.InventoryOpenFeignClient;
import com.notzed.ecommerce.order_service.dto.OrderDto;
import com.notzed.ecommerce.order_service.dto.OrderRequestDto;
import com.notzed.ecommerce.order_service.entity.Order;
import com.notzed.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/helloOrders")
    public String helloOrders(){
        return "Hello from Orders Service";
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto){
        OrderRequestDto orderRequestDto1 =orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(orderRequestDto1);
    }

    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(){
        log.info("Fetching all the orders via controller.");
        List<OrderRequestDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long orderId){
        log.info("Fetching the order with ID: {}" , orderId);
        OrderRequestDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
        log.info("Cancelling the order with ID: {}", orderId);
        OrderDto order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(order);
    }

}
