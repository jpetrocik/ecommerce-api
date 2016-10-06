package org.psoft.ecommerce.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.data.model.Shipment;
import org.psoft.ecommerce.plugins.OrderProcessorPlugin;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class OrderProcessorService extends AbstractService implements OrderProcessorInterface {
	private static final Log log = LogFactory.getLog(OrderProcessorService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.psoft.ecommerce.service.WarehouserInterface#generateShippingOrders()
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void submitApprovedOrders() {
		List<Order> orders = findAllApprovedOrders();

		OrderProcessorPlugin orderProcessorPlugin = (OrderProcessorPlugin) lookupPlugin(OrderProcessorPlugin.class);
		try {
			orderProcessorPlugin.submitOrders(orders);

			// update order status
			Session session = currentSession();

			for (Order order : orders) {
				order.setStatus(Order.STATUS_PROCESSING);
				session.saveOrUpdate(order);
			}
		} catch (Exception e) {
			log.error("Unable to submit orders for processing, plugin: " + orderProcessorPlugin.getClass().getCanonicalName() + " throw an exception",e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Order> findAllApprovedOrders() {
		List<Order> orders = currentSession().createCriteria(Order.class).add(Restrictions.eq("status", Order.STATUS_APPROVED))
				.list();

		return orders;
	}

}
