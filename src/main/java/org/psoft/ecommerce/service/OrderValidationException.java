package org.psoft.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class OrderValidationException extends Exception {
	
	final List<Exception> exception = new ArrayList<Exception>();
	
	public void addException(Exception e){
		exception.add(e);
	}
	
	public List<Exception> getExceptions(){
		return exception;
	}

	@Override
	public String getMessage() {
		return exception.get(0).getMessage();
	}
	
}

