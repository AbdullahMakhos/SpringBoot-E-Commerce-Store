package com.ecommerce.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.store.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Basic CRUD operations are provided by JpaRepository
    // You can add custom query methods here
}