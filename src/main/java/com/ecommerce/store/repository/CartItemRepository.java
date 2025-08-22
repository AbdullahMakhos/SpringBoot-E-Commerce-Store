package com.ecommerce.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.store.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
