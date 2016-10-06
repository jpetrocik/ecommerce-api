package org.psoft.ecommerce.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Coupon;
import org.psoft.ecommerce.data.model.CouponValues;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.data.model.OrderDtl;
import org.psoft.ecommerce.data.model.ShippingLevel;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestOrder extends UnitilsJUnit4 {

	@HibernateSessionFactory
	private SessionFactory sessionFactory;

	@Test
	public void testCreate() throws Exception {

		Session session = sessionFactory.getCurrentSession();

		Account account = (Account) session.get(Account.class, 1L);

		Item item = (Item) session.get(Item.class,1L);

		ShippingLevel shippingLevel = (ShippingLevel) session.get(ShippingLevel.class,1L);


		//Create OrderDtl
		OrderDtl orderDtl = new OrderDtl();
		orderDtl.setItem(item);
		orderDtl.setQty(1);
		orderDtl.setPrice(10.0);
		orderDtl.setLineNumber(1);
		
		//Create Order
		Order order = new Order();
		order.addDetail(orderDtl);
		order.setAccount(account);
		order.setBillingAddress(account.getBillingAddress().copy());
		order.setShippingAddress(account.getShippingAddress().copy());
		order.setShippingLevel(shippingLevel);

		session.saveOrUpdate(order);
	}
	
//	@Test
//	@ExpectedDataSet
	public void testCoupons() throws Exception{
		Session session = sessionFactory.getCurrentSession();

		CouponValues couponValues = (CouponValues)session.get(CouponValues.class, 1L);
		Coupon coupon = CouponValues.createCoupon(couponValues);
		Account account = (Account) session.get(Account.class, 1L);
		ShippingLevel shippingLevel = (ShippingLevel) session.get(ShippingLevel.class,1L);

		Order order = new Order();
		order.setAccount(account);
		order.setBillingAddress(account.getBillingAddress().copy());
		order.setShippingAddress(account.getShippingAddress().copy());
		order.setShippingLevel(shippingLevel);
		order.addCoupon(coupon);
		
		session.saveOrUpdate(order);
		
	}
	
//	@Test
//	@ExpectedDataSet
	public void testCoupons_withMultipleCoupons() throws Exception{
		Session session = sessionFactory.getCurrentSession();

		CouponValues couponValues1 = (CouponValues)session.get(CouponValues.class, 1L);
		Coupon coupon1 = CouponValues.createCoupon(couponValues1);
		CouponValues couponValues2 = (CouponValues)session.get(CouponValues.class, 2L);
		Coupon coupon2 = CouponValues.createCoupon(couponValues2);

		Account account = (Account) session.get(Account.class, 1L);
		ShippingLevel shippingLevel = (ShippingLevel) session.get(ShippingLevel.class,1L);

		Order order = new Order();
		order.setAccount(account);
		order.setBillingAddress(account.getBillingAddress().copy());
		order.setShippingAddress(account.getShippingAddress().copy());
		order.setShippingLevel(shippingLevel);
		order.addCoupon(coupon1);
		order.addCoupon(coupon2);
		
		session.saveOrUpdate(order);
		
	}
}
