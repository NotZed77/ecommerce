package com.ecommerce.shipping_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    private LocalDate deliveryDate;
}
