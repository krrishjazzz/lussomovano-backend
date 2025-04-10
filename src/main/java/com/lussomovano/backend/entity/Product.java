package com.lussomovano.backend.entity;

import jakarta.persistence.*;
import lombok.Data;


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
    @Column(name = "collection")
    private String collection;


    public boolean isOutOfStock() {
        return stockQuantity <= 0;
    }}

