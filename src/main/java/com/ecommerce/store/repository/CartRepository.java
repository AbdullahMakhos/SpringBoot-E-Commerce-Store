package com.ecommerce.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.store.model.Cart;
import com.ecommerce.store.model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user); // Crucial: find user's cart 
}