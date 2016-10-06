package org.psoft.ecommerce.plugins;

import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.service.PaymentException;

public interface CreditCardPlugin extends Plugin {

	/**
	 * Determines whether this PaymentPlugin support given payment
	 * 
	 * @param payment
	 * @return
	 */
	public boolean accepts(Payment payment);
	
	public void processAuthorization(Payment payment) throws PaymentException;

	public void processCapture(Payment payment, Double amount) throws PaymentException;

}
