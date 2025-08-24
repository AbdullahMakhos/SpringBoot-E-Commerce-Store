package com.ecommerce.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.store.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Basic CRUD operations are provided by JpaRepository
    // You can add custom query methods here
	User findByUsername(String string);

	User findByEmail(String email);
}