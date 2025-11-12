package org.example.barber_shop.dto.service;

import lombok.Data;

@Data
public class ServiceResponse {
    private Long id;
    private String name;
    private String description;
    private Integer durationMinutes;
    private Double price;
}
