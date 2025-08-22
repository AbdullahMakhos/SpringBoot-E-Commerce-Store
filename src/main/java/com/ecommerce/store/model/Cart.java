package com.ecommerce.store.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>(); // Initialize the list!

	public Cart() {}
    
    public Cart(Long cartId, User user, List<CartItem> items) {
		this.cartId = cartId;
		this.user = user;
		this.items = items;
	}
    
    public Long getCartId() {
		return cartId;
	}

	public User getUser() {
		return user;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	
	public List<Product> getProductsList(){
		return items.stream().map(CartItem::getProduct)
			   .toList();
	}
	
	public void removeItemByProductId(Long productId) {
	    Iterator<CartItem> iterator = this.items.iterator();
	    
	    while (iterator.hasNext()) {
	    	CartItem item = iterator.next();
	        if (item.getProduct().getProductID().equals(productId)) {
	            iterator.remove(); // Remove the item from the list
	            item.setCart(null); // Break the relationship for JPA
	        }
	    }
	}
}
