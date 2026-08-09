package com.ecommerce.shipping_service.repository;

import com.ecommerce.shipping_service.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {
}
