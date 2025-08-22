package com.ecommerce.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.store.model.Cart;
import com.ecommerce.store.model.Product;
import com.ecommerce.store.model.User;
import com.ecommerce.store.repository.CartItemRepository;
import com.ecommerce.store.repository.CartRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.repository.UserRepository;
import com.ecommerce.store.service.MainService;

@DataJpaTest // Uses an in-memory H2 database instead of your real MySQL for isolated testing
public class MainServiceTest {

    @Autowired
    private TestEntityManager entityManager; // Helper to save objects to test database

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private MainService mainService;

    @BeforeEach
    void setUp() {
        // Create a mock PasswordEncoder to avoid testing real encryption in this test
        PasswordEncoder mockEncoder = mock(PasswordEncoder.class);
        // Make the mock return a dummy value regardless of input
        when(mockEncoder.encode(anyString())).thenReturn("hashed_password");
        
        mainService = new MainService(productRepository, userRepository
		, cartRepository, cartItemRepository, mockEncoder);
    }

    @Test
    public void testAddProductToCart() {
        // 1. SETUP: Create and save a User and Product to the test database
        User user = new User("testuser", "password", "test@email.com");
        entityManager.persist(user); // Saves the user to H2 database, generates an ID

        Product product = new Product("Test Product", 19.99);
        entityManager.persist(product); // Saves the product to H2 database

        // 2. EXECUTE: Call the method we're actually testing
        Cart cart = mainService.addProductToCart(user, product.getProductID());

        // 3. VERIFY: Check that the method worked correctly
        assertThat(cart != null); // Ensure a cart was returned
        assertThat(cart.getItems()).hasSize(1); // Cart should have exactly 1 item
        assertThat(cart.getItems().get(0).getProduct().getProductName())
            .isEqualTo("Test Product"); // The item should be the product we added
    }
}