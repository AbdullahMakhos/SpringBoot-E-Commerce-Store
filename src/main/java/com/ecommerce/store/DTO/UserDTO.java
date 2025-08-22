package com.ecommerce.store.DTO;

import com.ecommerce.store.model.User;

public class UserDTO {
	private String username;
	private String email;
	
	public UserDTO(User user){
		username = user.getUsername();
		email = user.getEmail();
	}
	
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
