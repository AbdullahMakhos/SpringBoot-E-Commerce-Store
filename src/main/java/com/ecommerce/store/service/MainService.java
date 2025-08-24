package com.ecommerce.store.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.ecommerce.store.DTO.CartItemDTO;
import com.ecommerce.store.customizedExceptionHandler.BadBodyRequestException;
import com.ecommerce.store.customizedExceptionHandler.ProductNotFoundException;
import com.ecommerce.store.customizedExceptionHandler.UserNotFoundException;
import com.ecommerce.store.model.Cart;
import com.ecommerce.store.model.CartItem;
import com.ecommerce.store.model.Product;
import com.ecommerce.store.model.User;
import com.ecommerce.store.repository.CartItemRepository;
import com.ecommerce.store.repository.CartRepository;
import com.ecommerce.store.repository.ProductRepository;
import com.ecommerce.store.repository.UserRepository;

@Service
public class MainService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final PasswordEncoder passwordEncoder;

	public MainService(ProductRepository productRepository
	, UserRepository userRepository, CartRepository cartRepository
	, CartItemRepository cartItemRepository , PasswordEncoder passwordEncoder) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	//signup methods
	
	public User createUser(User user) {
	    // Check if username already exists
	    if (userRepository.findByUsername(user.getUsername()) != null) {
	        throw new RuntimeException("Username already exists");
	    }
	    
	    user = postUser(user);
	    return userRepository.save(user);
	}
	
	public boolean validateSignUp(String username, String password, String email) {
		return !emailExists(email) && !usernameExists(username);
	}

	//login methods
	
	public boolean validateLogin(String username, String plainPassword) {
	    User user = userRepository.findByUsername(username);
	    return user != null && passwordEncoder.matches(plainPassword, user.getPassword());
	}
	
	// cart methods
	
	public Cart getOrCreateCartForUser(User user) {
	    Cart cart = cartRepository.findByUser(user);
	    if (cart == null) {
	        cart = new Cart();
	        cart.setUser(user);
	        cartRepository.save(cart);
	    }
	    return cart;
	}

	public List<CartItemDTO> getActiveCartItems(User user) {
	    Cart cart = getOrCreateCartForUser(user);
	    return cart.getItems().stream()
	            .filter(item -> !item.isPurchased())
	            .map(item -> new CartItemDTO(item))
	            .collect(Collectors.toList());
	}
	public Cart addProductToCart(User user, Long productId) {
	    
		Cart cart = getOrCreateCartForUser(user);
		
	    Product product = productRepository.findById(productId).orElseThrow(() -> 
	        new ProductNotFoundException("Product not found with id: " + productId));

	    // Check if product already exists in cart
	    Optional<CartItem> existingItem = cart.getItems().stream()
	        .filter(item -> item.getProduct().getProductID().equals(productId))
	        .findFirst();

	    if (!existingItem.isPresent()) {
	        // Create new cart item
	        CartItem newItem = new CartItem();
	        newItem.setCart(cart);
	        newItem.setProduct(product);
	        cart.getItems().add(newItem);
	    }
	    return saveCart(cart);
	}

	public Cart saveCart(Cart cart) {
		return cartRepository.save(cart);
	}
	
	//users methods
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow();
	}
	
	public User getUserByUsername(String string) {
		return userRepository.findByUsername(string);
	}
	
	public User postUser(User user) {
		
		//validation 
		if (user.getUsername() == null || user.getUsername().isBlank()) {
		    throw new BadBodyRequestException("Username cannot be empty");
		}
		if (user.getPassword() == null || user.getPassword().length() < 3) {
		    throw new BadBodyRequestException("Password must be ≥ 3 characters");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepository.save(user);
	}
	
	public void deleteUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			cartRepository.deleteById(cartRepository.findByUser(user.get()).getCartId());
			userRepository.deleteById(id);	
		}else {
			throw new UserNotFoundException("User Not Found With ID :" + id);
		}
	}
	
	public boolean usernameExists(String username) {
		return userRepository.findByUsername(username) != null;
	}
	
	public boolean emailExists(String email) {
		return userRepository.findByEmail(email) != null;
	}
	//you can only change password
	public User changePassword(Long id,User newPasswordUser) {
		User existing = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException
				("User not found with id: " + id));
	
		if(newPasswordUser.getPassword() == null || newPasswordUser.getPassword().isBlank()) {
			throw  new BadBodyRequestException("password cannot be empty");
		}
		
		existing.setPassword(passwordEncoder.encode(newPasswordUser.getPassword()));
		
		return userRepository.save(existing);
	}

	//products methods
	
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	public List<Product> getAllProductsNotInUserCart(User user) {

		Iterator<Product> iterator = getAllProducts().iterator();
	
		List<Product> cartProducts = getOrCreateCartForUser(user).getProductsList();
		
		List<Product> responseProducts = new ArrayList<>();
		
	    while (iterator.hasNext()) {
	    	Product product = iterator.next();
	    	if(cartProducts.indexOf(product) == -1) {
	    		responseProducts.add(product);
	    	}
	    }
		return responseProducts;
	}
	
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException
				("Product not found with id: " + id));
	}

	public Product postProduct(Product product) {
		return productRepository.save(product);
	}

	public void deleteProductById(Long id) {
		productRepository.deleteById(id);
	}

	public Product patchProduct(Long id,Product product) {
		Product existing = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException
				("Product not found with id: " + id));
		
		if (product.getProductName() == null || product.getProductName().isBlank()) {
		    throw new BadBodyRequestException("Name cannot be empty");
		}
		
		if (product.getProductPrice() == null || product.getProductPrice() < 0.1) {
		    throw new BadBodyRequestException("Price must be ≥ 0.1");
		}
	
		existing.setProductName(product.getProductName());
		existing.setProductPrice(product.getProductPrice());
		
		return productRepository.save(existing);
	}

	public void removeProductFromCart(Cart cart, Long productId) {
	    cart.removeItemByProductId(productId);
	    cartRepository.save(cart);
	}	
}
