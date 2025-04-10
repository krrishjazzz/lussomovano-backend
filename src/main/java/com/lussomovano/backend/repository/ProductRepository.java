package com.lussomovano.backend.repository;

import com.lussomovano.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCollectionIgnoreCase(String collection); // for filtering by collection

    List<Product> findByCategoryIgnoreCaseAndCollectionIgnoreCase(String category, String collection);
}
