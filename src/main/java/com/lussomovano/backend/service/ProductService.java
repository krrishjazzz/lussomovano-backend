package com.lussomovano.backend.service;

import com.lussomovano.backend.entity.Product;
import com.lussomovano.backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
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

    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public List<Product> getByCollection(String collection) {
        return repo.findByCollectionIgnoreCase(collection);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getById(id);
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setTheme(updatedProduct.getTheme());
        existing.setCollection(updatedProduct.getCollection());
        existing.setImageUrl(updatedProduct.getImageUrl());
        return repo.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        repo.deleteById(id);
    }

    public List<Product> filterByCategoryAndCollection(String category, String collection) {
        return repo.findByCategoryIgnoreCaseAndCollectionIgnoreCase(category, collection);
    };

    public List<Product> advancedFilter(String name, String category, String color, String theme,
                                        String collection, Double minPrice, Double maxPrice) {
        return repo.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty())
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            if (category != null && !category.isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
            if (color != null && !color.isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("color")), color.toLowerCase()));
            if (theme != null && !theme.isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("theme")), theme.toLowerCase()));
            if (collection != null && !collection.isEmpty())
                predicates.add(cb.equal(cb.lower(root.get("collection")), collection.toLowerCase()));
            if (minPrice != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            if (maxPrice != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
