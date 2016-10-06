package org.psoft.ecommerce.service;

public interface OrderProcessorInterface {

	/**
	 * Called by the system to drop order
	 */
	public abstract void submitApprovedOrders();

}