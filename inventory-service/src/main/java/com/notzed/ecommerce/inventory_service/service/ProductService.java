package com.notzed.ecommerce.inventory_service.service;

import com.notzed.ecommerce.inventory_service.dto.OrderItemRequestDto;
import com.notzed.ecommerce.inventory_service.dto.OrderRequestDto;
import com.notzed.ecommerce.inventory_service.dto.ProductDto;
import com.notzed.ecommerce.inventory_service.entity.Product;
import com.notzed.ecommerce.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public List<ProductDto> getAllInventory(){
        log.info("Fetching all inventory items");
        List<Product> inventories = productRepository.findAll();
        return inventories.stream()
                .map((element) -> modelMapper.map(element, ProductDto.class))
                .toList();
    }

    public ProductDto getProductById(Long id){
        log.info("Fetching the Product with ID: {}", id);
        Optional<Product> inventory = productRepository.findById(id);
        return inventory.map((element) ->
                modelMapper.map(element, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Product not found with ID: {}"+ id));
    }

    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {
        log.info("Reducing the stocks");
         Double totalPrice = 0.0;
        if (orderRequestDto.getItems() == null || orderRequestDto.getItems().isEmpty()) {
            throw new RuntimeException("Order items cannot be empty");
        }
        for(OrderItemRequestDto orderItemRequestDto: orderRequestDto.getItems()){
            Long productId = orderItemRequestDto.getProductId();
            Integer quantity = orderItemRequestDto.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: {}" + productId));

            if(product.getStock() < quantity){
                throw new RuntimeException("Product cannot be fulfilled for given quantity");
            }

            product.setStock(product.getStock()-quantity);
            productRepository.save(product);
            totalPrice += quantity*product.getPrice();
        }
        return totalPrice;
    }

    public void restock(Long productId, int quantity){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: {}"+ productId));
        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }

}
