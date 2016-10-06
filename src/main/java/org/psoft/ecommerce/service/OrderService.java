package org.psoft.ecommerce.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.psoft.ecommerce.data.model.Order;

public class OrderService extends AbstractService<Order> {
	private static Log log = LogFactory.getLog(OrderService.class);

	@SuppressWarnings("unchecked")
	public List<Order> searchOrders(String name, String email, String orderNum, String status) {
		Session session = currentSession();

		log.debug("Search account (" + name + " , " + email + " , " + orderNum + ")");

		Criteria criteria = session.createCriteria(Order.class);

		if (StringUtils.isNotBlank(name))
			criteria.createCriteria("billingAddress").add(Restrictions.ilike("name", name));

		if (StringUtils.isNotBlank(email))
			criteria.createCriteria("account").add(Restrictions.ilike("email", email));

		if (StringUtils.isNotBlank(orderNum))
			criteria.add(Restrictions.ilike("orderNum", orderNum));

		if (StringUtils.isNotBlank(status))
			criteria.add(Restrictions.eq("status", status));

		criteria.addOrder(org.hibernate.criterion.Order.desc("createdOn"));
		return (List<Order>) criteria.list();
	}

	public Order retrieveById(Long orderId) {
		Session session = currentSession();

		Order order = (Order) session.get(Order.class, orderId);

		return order;

	}

	public Order cancelOrder(Long orderId) {
		Session session = currentSession();

		Order order = retrieveById(orderId);

		if (!Order.STATUS_PROCESSING.equals(order.getStatus())) {
			throw new IllegalStateException("Order must be in " + Order.STATUS_PROCESSING + " status to cancel it");
		}

		order.setStatus(Order.STATUS_CANCELED);

		session.saveOrUpdate(order);
		session.flush();

		return order;

	}
	
	/**
	 * Fetches order history for an account id
	 * 
	 * @param accountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Order> retireveOrderHistoryByAccountId(Long accountId) {
		Session session = currentSession();

		Query q = session.createQuery("from Order as a where account_id = :accountId order by createdOn desc");
		q.setLong("accountId", accountId);

		List<Order> orders = (List<Order>) q.list();

		return orders;
	}

	/**
	 * Fetches an order with order id and account id.
	 * 
	 * @param orderId
	 * @param accountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Order retrieveOrder(Long orderId, Long accountId) {
		Session session = currentSession();

		Query q = session.createQuery("from Order as a where id = :orderId and account_id = :accountId");
		q.setLong("orderId", orderId);
		q.setLong("accountId", accountId);

		return (Order) get(q);

	}


}
