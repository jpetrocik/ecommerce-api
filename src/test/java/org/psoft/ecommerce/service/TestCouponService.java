package org.psoft.ecommerce.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.coupons.DiscountAmtCoupon;
import org.psoft.ecommerce.coupons.DiscountPcntCoupon;
import org.psoft.ecommerce.coupons.FreeShippingCoupon;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Coupon;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestCouponService extends UnitilsJUnit4 {

	private Date startDate = new GregorianCalendar(2009, 1, 1).getTime();

	private Date endDate = new GregorianCalendar(2020, 12, 31).getTime();

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	CouponService couponService;

	AccountType accountType;
	
	@Before
	public void setup() {
		couponService = new CouponService();
		couponService.setSessionFactory(sessionFactory);

		Session session = sessionFactory.getCurrentSession();
		
		accountType = (AccountType)session.get(AccountType.class, 1L);
	}

	@Test
	public void testDiscountAmtCoupon() {

		
		DiscountAmtCoupon coupon = new DiscountAmtCoupon();
		coupon.setAccountType(accountType);
		coupon.setCode("50OFF");
		coupon.setActive(true);
		coupon.setStartDate(startDate);
		coupon.setEndDate(endDate);
		coupon.setDescr("Test Coupon");
		coupon.setExculsive(true);
		coupon.setValue1("25");
		coupon.setValue2("5");
		coupon.setValue3("50");
		coupon.setValue4("10");
		coupon.setValue5("100");
		coupon.setValue6("25");
		coupon.setValue7("200");
		coupon.setValue8("50");
		couponService.save(coupon.update());

		Coupon dac = couponService.getCouponByCode("50OFF");
		assertEquals(coupon, dac);
	}

	@Test
	public void testDiscountPcntCoupon() {

		DiscountPcntCoupon coupon = new DiscountPcntCoupon();
		coupon.setAccountType(accountType);
		coupon.setCode("10PCNT");
		coupon.setActive(true);
		coupon.setStartDate(startDate);
		coupon.setEndDate(endDate);
		coupon.setDescr("Test Percentage Coupon");
		coupon.setExculsive(true);
		coupon.setValue1("50");
		coupon.setValue2("10");
		couponService.save(coupon.update());

		Coupon dpc = couponService.getCouponByCode("10PCNT");
		assertEquals(coupon, dpc);
	}

	@Test
	public void testFreeShippingCoupon() {

		FreeShippingCoupon coupon = new FreeShippingCoupon();
		coupon.setAccountType(accountType);
		coupon.setCode("50FREE");
		coupon.setActive(true);
		coupon.setStartDate(startDate);
		coupon.setEndDate(endDate);
		coupon.setDescr("Test Percentage Coupon");
		coupon.setExculsive(true);
		coupon.setValue1("50");
		coupon.setValue2("GND");
		couponService.save(coupon.update());

		Coupon fsc = couponService.getCouponByCode("50FREE");
		assertEquals(coupon, fsc);
	}

	@Test
	public void testSearchCoupon() {

		List<Coupon> results = couponService.searchCoupon("35FREE", true);
		assertEquals(1, results.size());
	}

}
