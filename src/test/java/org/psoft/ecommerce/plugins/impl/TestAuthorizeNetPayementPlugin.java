package org.psoft.ecommerce.plugins.impl;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.data.model.OrderDtl;
import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.util.ApplicationSettings;
import org.psoft.ecommerce.util.DateServerUtil;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TestAuthorizeNetPayementPlugin extends TestCase {

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	AuthorizeNetPaymentPlugin authorizeNetPaymentPlugin;

	ApplicationSettings applicationSettings;

	@Before
	public void setUp() {
		
		applicationSettings = new ApplicationSettings();
		applicationSettings.setSessionFactory(sessionFactory);
		
		authorizeNetPaymentPlugin = new AuthorizeNetPaymentPlugin();
		authorizeNetPaymentPlugin.setSessionFactory(sessionFactory);
		authorizeNetPaymentPlugin.setApplicationSettings(applicationSettings);
	}

	private Order createOrder() {

		Order order = new Order();
		Address shippingAddress = new Address();
		shippingAddress.setName("John Doe");
		shippingAddress.setAddress1("555 Someplace Ave.");
		shippingAddress.setCity("Dreamland");
		shippingAddress.setState("CA");
		shippingAddress.setPostalCode("99999");
		shippingAddress.setPhone("555-555-1234");

		Address billingAddress = new Address();
		billingAddress.setName("John Doe");
		billingAddress.setAddress1("555 Someplace Ave.");
		billingAddress.setCity("Dreamland");
		billingAddress.setState("CA");
		billingAddress.setPostalCode("99999");
		billingAddress.setPhone("555-555-1234");

		OrderDtl line1 = new OrderDtl();
		line1.setPrice(1.00);
		line1.setQty(2);

		OrderDtl line2 = new OrderDtl();
		line2.setPrice(1.00);
		line2.setQty(2);

		Account account = new Account();
		account.setEmail("john@domain.com");

		order.setAccount(account);
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		order.setTax(.16);
		order.setDiscount(0.0);
		order.setTotal(2.16);
		order.setSubtotal(2.0);
		order.setShipping(0.0);

		order.addDetail(line1);
		order.addDetail(line2);
		order.setCreatedOn(DateServerUtil.currentDate().getTime());

		return order;
	}

	@Test
	public void testAuthorization() throws Exception {

		Order order = createOrder();
		Payment payment = new Payment();
		payment.setAccount("4111111111111111");
		payment.setExpires("0215");
		payment.setStatus(Payment.NEW);
		payment.setType("MC");
		payment.setAmount(order.getTotal());
		payment.setCvn("000");

		order.addPayment(payment);

		authorizeNetPaymentPlugin.processAuthorization((Payment) order.getPayments().iterator().next());

		assertEquals("Wrong payment status", "authorized", payment.getStatus());
	}

	@Test
	public void testAuthorizationFailure() throws Exception {

		Order order = createOrder();
		Payment payment = new Payment();
		payment.setAccount("4222222222222");
		payment.setExpires("0212");
		payment.setStatus(Payment.NEW);
		payment.setType("MC");
		payment.setAmount(order.getTotal());
		payment.setCvn("000");

		order.addPayment(payment);

		authorizeNetPaymentPlugin.processAuthorization(payment);

		assertEquals("Wrong payment status", "failed", payment.getStatus());
	}

}
