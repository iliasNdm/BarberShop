package org.example.barber_shop.dao.entities;

import jakarta.persistence.Id;

public class Material {
    @Id
    private Long id;
    private String name;
    private Integer quantity;
    private String unitOfMeasure;
}

