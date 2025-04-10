package com.lussomovano.backend.controller;

import com.lussomovano.backend.entity.Product;
import com.lussomovano.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    //    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        logger.info("Received request to add product: {}", product);
        Product savedProduct = productService.saveProduct(product);
        logger.info("Saved product: {}", savedProduct);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productService.getAll();
        logger.info("Total products found: {}", products.size());
        return products;
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        logger.info("Fetching products by category: {}", category);
        List<Product> products = productService.getByCategory(category);
        logger.info("Products found for category '{}': {}", category, products.size());
        return products;
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        logger.info("Searching products with keyword: {}", keyword);
        List<Product> results = productService.search(keyword);
        logger.info("Search results count: {}", results.size());
        return results;
    }

    @GetMapping("/jungle-drop")
    public List<Product> getJungleDrop() {
        logger.info("Fetching jungle drop products");
        List<Product> products = productService.getByTheme("jungle");
        logger.info("Jungle drop products found: {}", products.size());
        return products;
    }
}
