package org.psoft.ecommerce.plugins.impl;

import java.util.HashMap;
import java.util.Map;

import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.plugins.AbstractPlugin;
import org.psoft.ecommerce.plugins.CreditCardPlugin;
import org.psoft.ecommerce.service.PaymentException;

public class Net30PaymentPlugin extends AbstractPlugin implements CreditCardPlugin {

	public static String NET30 = "Net30";
	
	public void processAuthorization(Payment payment) throws PaymentException {
		payment.setStatus(Payment.AUTH);
	}

	public void processCapture(Payment payment, Double amount) throws PaymentException {
		payment.setStatus(Payment.CAPTURED);
	}

	public String getName() {
		return "Net30 Payment";
	}

	public Map<String, Object> getSettings() {
		Map<String, Object> settings = new HashMap<String, Object>();

		return settings;
	}

	public boolean accepts(Payment payment) {
		return payment.getType().equals(Net30PaymentPlugin.NET30);
	}

}
