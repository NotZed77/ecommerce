package com.notzed.ecommerce.order_service.service;

import com.ecommerce.shipping_service.dto.ShippingDto;
import com.notzed.ecommerce.order_service.client.InventoryOpenFeignClient;
import com.notzed.ecommerce.order_service.client.ShippingOpenFeignClient;
import com.notzed.ecommerce.order_service.dto.OrderDto;
import com.notzed.ecommerce.order_service.dto.OrderRequestDto;
import com.notzed.ecommerce.order_service.entity.Order;
import com.notzed.ecommerce.order_service.entity.OrderItem;
import com.notzed.ecommerce.order_service.entity.OrderStatus;
import com.notzed.ecommerce.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;
    private final ShippingOpenFeignClient shippingOpenFeignClient;

    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map((element) -> modelMapper.map(element, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: {}" + id));
        return modelMapper.map(order, OrderRequestDto.class);
    }

    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        Double totalPrice = reduceStocks(orderRequestDto);

        Order order = modelMapper.map(orderRequestDto, Order.class);
        log.info("Calling the createOrder method");

        for (OrderItem orderItem : order.getItems()) {
            orderItem.setOrder(order);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        ShippingDto shipping = confirmShipping(order.getShippingId());

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "reduceStocksFallback")
    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "reduceStocksFallback")
    public Double reduceStocks(OrderRequestDto orderRequestDto){
        return inventoryOpenFeignClient.reduceStocks(orderRequestDto);
    }

    @Retry(name = "shippingService", fallbackMethod = "shippingFallback")
    @CircuitBreaker(name = "shippingCircuitBreaker", fallbackMethod = "shippingFallback")
    public ShippingDto confirmShipping(Long shippingId){
        return shippingOpenFeignClient.confirmedShipping(String.valueOf(shippingId));
    }

    public ShippingDto shippingFallback(Long shippingId, Throwable throwable){
        log.error("Shipping Service unavailable for Shipping ID {}: {}", shippingId, throwable.getMessage());
        throw new RuntimeException("Shipping Service is currently unavailable");
    }

    public Double reduceStocksFallback(OrderRequestDto orderRequestDto, Throwable throwable){
        log.error("Inventory Service unavailable: {}",throwable.getMessage());
        throw new RuntimeException("Inventory Service is currently unavailable");
    }

    public OrderDto cancelOrder(Long orderId){
        log.info("Cancelling order with ID: {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow
                (() -> new RuntimeException("Order is already cancelled"));
        if(order.getOrderStatus() == OrderStatus.CANCELLED){
            throw new RuntimeException("Order is already cancelled");
        }
        for(OrderItem item: order.getItems()){
            inventoryOpenFeignClient.restock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }
}