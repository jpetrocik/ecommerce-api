package org.psoft.ecommerce.service;

public class UserException extends Exception {

	private String resourceKey;

	private Object[] args;

	public UserException(String resourceKey, Object[] args) {
		this.resourceKey = resourceKey;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public String getResourceKey() {
		return resourceKey;
	}

}
