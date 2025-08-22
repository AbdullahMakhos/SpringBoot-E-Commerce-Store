package com.ecommerce.store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;
    
    @Column(nullable = false, length = 100)
    private String productName;
    
    @Column(nullable = false)
    private Double productPrice;
    
    public Product() {}  // Required by JPA
    
    public Product(Long productID, String productName, Double productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
    }
    
    //for tests
    public Product(String productName, Double productPrice) {
    	this.productName = productName;
        this.productPrice = productPrice;
    }

	public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    
    @Override
    public String toString() {
        return "Product [productID=" + productID + ", productName=" + productName + ", productPrice=" + productPrice
                + "]";
    }
}