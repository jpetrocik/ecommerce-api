package org.psoft.ecommerce.rest.view;

public class EcommerceException extends RuntimeException {
	
	int errorCode;
	
	protected EcommerceException(int errorCode, String message) {
		super(message);
		
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
