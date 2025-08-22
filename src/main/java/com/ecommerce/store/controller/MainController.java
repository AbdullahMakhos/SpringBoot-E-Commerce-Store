package com.ecommerce.store.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecommerce.store.DTO.CartDTO;
import com.ecommerce.store.DTO.CartItemDTO;
import com.ecommerce.store.DTO.PurchaseDTO;
import com.ecommerce.store.DTO.UserDTO;
import com.ecommerce.store.model.Cart;
import com.ecommerce.store.model.CartItem;
import com.ecommerce.store.model.Product;
import com.ecommerce.store.model.User;
import com.ecommerce.store.service.MainService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    private final MainService mainService;
    
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }
    
    @GetMapping("/") 
    public String homePage(HttpSession session,Model model) { // The Model is how we pass data to the template
    	String username = (String) session.getAttribute("user");
    	//if not logged in -> log in page
        if(username == null) {
        	return "redirect:/signup-page";
        }
        User user = mainService.getUserByUsername(username);
        List<Product> allProducts = mainService.getAllProductsNotInUserCart(user);
        
        // Add the list to the model so Thymeleaf can use it
        model.addAttribute("products", allProducts);
        // Return the name of the Thymeleaf template (home.html)
        return "home-page";
    }
    
    //signup methods
    @GetMapping("/signup-page")
    public String showSignupForm() {
        return "signup-page"; // renders signup.html
    }

    @PostMapping("/signup-page")
    public String processSignup(@RequestParam String username
    ,@RequestParam String password,@RequestParam(required = false) String email
    ,HttpSession session) {
        
        try {
            User newUser = new User(username, password, email);
            mainService.createUser(newUser); // This should hash the password
            session.setAttribute("user", username);
            return "redirect:/"; // Redirect to home-page on success
            
        } catch (Exception e) {
            // Handle duplicate username etc.
            return "redirect:/signup?error";
        }
    }
    
    //login methods
    
    @GetMapping("/login-page")
    public String showLoginPage(HttpSession session) {
    	session.invalidate();
        return "login-page"; // Returns login-page.html template
    }
    @PostMapping("/login-page")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpSession session) {
    	if (mainService.validateLogin(username, password)) {
            session.setAttribute("user", username); // Store username in session
           return "redirect:/";
        } else {
            return "redirect:/login-page?error";
        }
    }      
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // clears the session
        return "redirect:/login-page"; // redirect to home page
    }
    
    //cart methods
    
    @PostMapping("/cart/add/{productId}")
    public String addProductToCart(@PathVariable Long productId, 
                                 HttpSession session) {
        String username = (String) session.getAttribute("user");
        User user = mainService.getUserByUsername(username);

        mainService.addProductToCart(user, productId);
        
        return "redirect:/cart-page"; 
    }
    
    @GetMapping("/cart-page")
    public String viewCart(HttpSession session, Model model) {
        // Check if user is logged in
        String username = (String) session.getAttribute("user");
        if(username == null) {
            return "redirect:/login-page";
        }


        User user = mainService.getUserByUsername(username);
        List<CartItemDTO> activeItems = mainService.getActiveCartItems(user); 
        
        model.addAttribute("cartItems", activeItems); 
        return "cart-page"; // This will render cart.html
    }
    
    @DeleteMapping("/cart/remove/{productId}")
    public String removeProductFromCart(HttpSession session
    		,@PathVariable Long productId) { 
        String username = (String) session.getAttribute("user");
        User user = mainService.getUserByUsername(username);
        Cart cart = mainService.getOrCreateCartForUser(user);
        
        mainService.removeProductFromCart(cart, productId);
        
        return "redirect:/cart-page";
    }
    
    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session) {
        String username = (String) session.getAttribute("user");
        User user = mainService.getUserByUsername(username);
        Cart cart = mainService.getOrCreateCartForUser(user);

        // Mark all items in the cart as 'purchased'
        for (CartItem item : cart.getItems()) {
            item.setPurchased(true);
        }
        
        mainService.saveCart(cart);
        	
        return "redirect:/order-confirmation";
    }
    
    @GetMapping("/order-confirmation")
    public String orderConfirmation(HttpSession session) {
        // Check if user is logged in
        String username = (String) session.getAttribute("user");
        if(username == null) {
            return "redirect:/login";
        }
        return "order-confirmation"; // order-confirmation.html
    }
    
    @GetMapping("/purchases")
    public List<PurchaseDTO> getPurchases(HttpSession session) {
        String username = (String) session.getAttribute("user");
        User user = mainService.getUserByUsername(username);
        Cart cart = mainService.getOrCreateCartForUser(user);

        return cart.getItems().stream()
                .filter(CartItem::isPurchased) //check if Purchased
                .map(item -> new PurchaseDTO(item)) // Convert to DTO
                .collect(Collectors.toList());
    }
    
    @GetMapping("/purchases-page")
    public String viewPurchasesPage(HttpSession session, Model model) {
    	// Check login
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "redirect:/login-page";
        }
        
        User user = mainService.getUserByUsername(username);
        Cart cart = mainService.getOrCreateCartForUser(user);

        List<PurchaseDTO> purchases = getPurchases(session);

        // Add the list to the model for Thymeleaf
        model.addAttribute("purchases", purchases);

        return "purchases-page"; // This will render purchases.html
    }
    
    
    
    @GetMapping("/cart")
    public ResponseEntity<CartDTO> showCart(HttpSession session){
    	String username = (String) session.getAttribute("user");
    	User user = mainService.getUserByUsername(username);
    	Cart cart = mainService.getOrCreateCartForUser(user);
    	CartDTO cartDTO = new CartDTO(cart);
    	
    	return ResponseEntity.ok(cartDTO);
    }
	//users methods
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
    	List<User> allUsers = mainService.getAllUsers();
    	return ResponseEntity.ok(allUsers);
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    	User user = mainService.getUserById(id);
    	UserDTO userDTO = new UserDTO(user);
        return ResponseEntity.ok(userDTO);
    }
    
    @PostMapping("/users")
    public ResponseEntity<UserDTO> postUser(@RequestBody User user) {
    	User savedUser = mainService.postUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getUserId())
                .toUri();
        
        UserDTO userDTO = new UserDTO(savedUser);
        return ResponseEntity.created(location).body(userDTO); 
    }
    
    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDTO> patchUser(@PathVariable Long id,@RequestBody User user) {
    	User patchedUser = mainService.changePassword(id,user);
    	UserDTO userDTO = new UserDTO(patchedUser);
        return ResponseEntity.ok(userDTO);
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        mainService.deleteUserById(userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    
    //products methods
    
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
    	List<Product> allProducts = mainService.getAllProducts();
    	return ResponseEntity.ok(allProducts);
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    	Product product = mainService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @PostMapping("/products")
    public ResponseEntity<Product> postProduct(@RequestBody Product product) {
        Product savedProduct = mainService.postProduct(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getProductID())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct); 
    }
    
    @PatchMapping("/products/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id,@RequestBody Product product) {
    	Product patchedProduct = mainService.patchProduct(id,product);
        return ResponseEntity.ok(patchedProduct);
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        mainService.deleteProductById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}