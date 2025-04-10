package com.lussomovano.backend.controller;

import com.lussomovano.backend.entity.Product;
import com.lussomovano.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    // Create Product (Admin)
    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        logger.info("Adding new product: {}", product);
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Update Product (Admin)
    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Updating product with id: {}", id);
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    // Delete Product (Admin)
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // Get All Products
    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productService.getAll();
    }



    // Get Products by Category
    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        logger.info("Fetching products by category: {}", category);
        return productService.getByCategory(category);
    }

    // Get Products by Collection
    @GetMapping("/collection/{collection}")
    public List<Product> getByCollection(@PathVariable String collection) {
        logger.info("Fetching products by collection: {}", collection);
        return productService.getByCollection(collection);
    }

    // Search Products by Keyword
    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        logger.info("Searching products with keyword: {}", keyword);
        return productService.search(keyword);
    }

    // Filter by Category & Collection
    @GetMapping("/filter")
    public List<Product> filterByCategoryAndCollection(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String collection) {

        logger.info("Filtering by category: {}, collection: {}", category, collection);
        return productService.filterByCategoryAndCollection(category, collection);
    }

    // Get Product by ID
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        logger.info("Fetching product by id: {}", id);
        return Optional.ofNullable(productService.getById(id));
    }

    @GetMapping("/filter/advanced")
    public List<Product> advancedFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) String collection,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        logger.info("Advanced filter params - name: {}, category: {}, color: {}, theme: {}, collection: {}, minPrice: {}, maxPrice: {}",
                name, category, color, theme, collection, minPrice, maxPrice);

        return productService.advancedFilter(name, category, color, theme, collection, minPrice, maxPrice);
    }
}
