package org.psoft.ecommerce.service;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Order;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestOrderService extends UnitilsJUnit4 {

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	OrderService orderService;

	@Before
	public void setUp() {
		orderService = new OrderService();
		orderService.setSessionFactory(sessionFactory);

	}

	@Test
	public void testCancelOrder() {

		orderService.cancelOrder(2L);

		Order order = orderService.retrieveById(2L);
		Assert.assertEquals("Order is not canceled", "Canceled", order.getStatus());

		try {
			orderService.cancelOrder(1L);
			new AssertionFailedError("Order is not cancelable");
		} catch (IllegalStateException ise) {
		}

	}

	@Test
	public void testRetrieveOrder() {
		Order order = orderService.retrieveById(1L);
		Assert.assertEquals("Wrong order retrieved", "100-1-1", order.getOrderNum());
	}

	@Test
	public void testSearchOrders() {

		List<Order> orders = orderService.searchOrders("John Doe", null, null, null);
		Assert.assertEquals("Wrong number of orders", 2, orders.size());

		orders = orderService.searchOrders(null, "jdoe@something.com", null, null);
		Assert.assertEquals("Wrong number of orders", 2, orders.size());

		orders = orderService.searchOrders(null, null, "100-1-1", "Shipped");
		Assert.assertEquals("Wrong number of orders", 1, orders.size());
	}

}
