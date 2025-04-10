package com.lussomovano.backend.service;

import com.lussomovano.backend.entity.Product;
import com.lussomovano.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> getAll() {
        return repo.findAll();
    }

    public List<Product> getByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    public List<Product> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    public List<Product> getByTheme(String theme) {
        return repo.findByThemeIgnoreCase(theme);
    }

    public Product saveProduct(Product product) {
        return repo.save(product);
    }
}

