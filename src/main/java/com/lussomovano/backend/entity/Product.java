package com.lussomovano.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;


@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String color;
    private String theme;
    private String imageUrl;
    private String category;
    private int stockQuantity;
    private String collectionName;



    public boolean isOutOfStock() {
        return stockQuantity <= 0;
    }}

