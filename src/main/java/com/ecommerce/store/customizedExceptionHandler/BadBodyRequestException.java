package com.ecommerce.store.customizedExceptionHandler;

@SuppressWarnings("serial")
public class BadBodyRequestException extends RuntimeException{
	public BadBodyRequestException(String message) {
        super(message);
    }
}
