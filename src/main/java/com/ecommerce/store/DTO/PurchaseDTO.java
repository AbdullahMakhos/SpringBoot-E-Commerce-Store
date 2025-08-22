package com.ecommerce.store.DTO;

import com.ecommerce.store.model.CartItem;

public class PurchaseDTO {
	private String productName;
	private double productPrice;
	
	public PurchaseDTO(CartItem item) {
	    this.productName = item.getProduct().getProductName();
	    this.productPrice = item.getProduct().getProductPrice();
	}
	
	public String getProductName() {
		return productName;
	}
	
	public double getProductPrice() {
		return productPrice;
	}
}
