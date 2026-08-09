package com.ecommerce.shipping_service.service;

import com.ecommerce.shipping_service.dto.ShippingDto;
import com.ecommerce.shipping_service.entity.Shipping;
import com.ecommerce.shipping_service.entity.ShippingStatus;
import com.ecommerce.shipping_service.repository.ShippingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ShippingDto confirmedShipping(Long shippingId){
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new RuntimeException("Shipping not found with ID: {}"+ shippingId));
        shipping.setShippingStatus(ShippingStatus.CONFIRMED);
        shippingRepository.save(shipping);
        return modelMapper.map(shipping, ShippingDto.class);
    }
}
