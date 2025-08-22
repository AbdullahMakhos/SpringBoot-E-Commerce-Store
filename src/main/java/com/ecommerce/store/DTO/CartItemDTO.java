package com.ecommerce.store.DTO;

import com.ecommerce.store.model.CartItem;

public class CartItemDTO {
    private Long productId;
    private String productName;
    private double productPrice;
    private boolean purchased;
    
    public CartItemDTO(CartItem cartItem) {
        this.productId = cartItem.getProduct().getProductID();
        this.productName = cartItem.getProduct().getProductName();
        this.productPrice = cartItem.getProduct().getProductPrice();
        this.purchased = cartItem.purchased;
    }

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public boolean isPurchased() {
		return purchased;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}
}