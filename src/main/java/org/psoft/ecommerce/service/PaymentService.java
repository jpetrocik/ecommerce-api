package org.psoft.ecommerce.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.FlushMode;
import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.plugins.CreditCardPlugin;
import org.psoft.ecommerce.plugins.Plugin;

public class PaymentService extends AbstractService<Payment> {

	public void processAuthorization(Payment payment) throws PaymentException {
		Collection<Plugin> allCreditCardPlugin =  pluginsByClass(CreditCardPlugin.class);

		for (Plugin aPlugin : allCreditCardPlugin){
			CreditCardPlugin creditCardPlugin = (CreditCardPlugin)aPlugin;
			
			if (creditCardPlugin.accepts(payment)){
				creditCardPlugin.processAuthorization(payment);
				break;
			}

		}

	}

	public void reAuthorize(Payment payment) throws PaymentException {
		
		//payment is fully captured, nothing to authorize
		if (payment.getAmount().compareTo(payment.getCapturedAmount()) < 1 ){
			return;
		}
		
		Payment newPayment = payment.duplicate();
		processAuthorization(newPayment);

		currentSession().save(newPayment);
	}
	
	public void reAuthorize(Order order) throws PaymentException {
		
		currentSession().setFlushMode(FlushMode.AUTO);
		
		//wrapped to prevent ConcurrentModificationException
		Set<Payment> payments = new HashSet<Payment>(order.getPayments());
		for (Payment payment : payments) {
			if (payment.getStatus().equals(Payment.AUTH)){
				reAuthorize(payment);
			}
		}

		currentSession().save(order);
		currentSession().flush();
	}
	
	public void processCapture(Payment payment, Double amount) throws PaymentException {
		Collection<Plugin> allCreditCardPlugin =  pluginsByClass(CreditCardPlugin.class);

		for (Plugin aPlugin : allCreditCardPlugin){
			CreditCardPlugin creditCardPlugin = (CreditCardPlugin)aPlugin;
			
			if (creditCardPlugin.accepts(payment)){
				creditCardPlugin.processCapture(payment, amount);
				break;
			}

		}
	}
}
