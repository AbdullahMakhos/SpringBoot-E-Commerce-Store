package com.ecommerce.store.DTO;

import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.store.model.Cart;

public class CartDTO {
    private List<CartItemDTO> items;
    
    public CartDTO(Cart cart) {
        this.items = cart.getItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }

	public List<CartItemDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}
    
}
