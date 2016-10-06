package org.psoft.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.math.NumberUtils;
import org.easymock.classextension.EasyMock;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.psoft.builders.ListBuilder;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Pricing;
import org.psoft.ecommerce.data.model.ShippingLevel;
import org.psoft.ecommerce.data.model.ShippingRate;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestCartService extends UnitilsJUnit4 {

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	@Mock
	private EmailService mockEmailService;

	@Mock
	private TaxService mockTaxService;

	@Mock
	private ShippingService mockShippingService;

	@Mock
	private PaymentService mockPaymnetService;

	@Mock
	private ItemService mockItemService;

	@Mock
	private CouponService mockCouponService;

	CartService cartService;

	@Before
	public void setUp() {
		cartService = new CartService();
		cartService.setEmailService(mockEmailService);
		cartService.setTaxService(mockTaxService);
		cartService.setShippingService(mockShippingService);
		cartService.setPaymentService(mockPaymnetService);
		cartService.setCouponService(mockCouponService);
		cartService.setItemService(mockItemService);
		cartService.setSessionFactory(sessionFactory);

	}

	@Test
	public void testSetShippingAddress() {
		Session session = sessionFactory.getCurrentSession();

		ShippingLevel shippingLevel = new ShippingLevel();
		shippingLevel.setDescription("Ground Level");
		shippingLevel.setServiceLevel("GRND");
		ShippingRate shippingRate = createShippingRate(10d, shippingLevel);

		Item item1 = (Item) session.get(Item.class, 1L);
		Item item2 = (Item) session.get(Item.class, 2L);
		
		Pricing price = new Pricing();
		price.setPrice(10.0);
		price.setMinimum(1);
		price.setMultiplier(1);
		

		EasyMock.expect(mockTaxService.calculateTax(null)).andReturn(BigDecimal.ZERO).anyTimes();
		EasyMock.expect(mockShippingService.lookupShippingOptions(null, null, null, null)).andReturn(
				new ListBuilder<ShippingRate>().add(shippingRate).getList()).anyTimes();
		EasyMock.expect(mockItemService.retireveCurrentPricing(item1, null)).andReturn(price).anyTimes();
		EasyMock.expect(mockItemService.retireveCurrentPricing(item2, null)).andReturn(price).anyTimes();
		EasyMockUnitils.replay();

		cartService.add(item1, new Integer(10));
		cartService.add(item2, new Integer(5));

		Assert.assertEquals(NumberUtils.DOUBLE_ZERO, cartService.getShipping());

		// Now set the shipping address and everything should recalculate
		Address shippingAddress = new Address();
		cartService.setShippingAddress(shippingAddress);

		Assert.assertEquals(new Double(10), cartService.getShipping());
		Assert.assertEquals("GRND", cartService.getShippingLevel().getServiceLevel());

		EasyMockUnitils.verify();

	}

	@Test
	public void testSetShippingLevel() {
		Session session = sessionFactory.getCurrentSession();

		ShippingLevel grnd = new ShippingLevel();
		grnd.setDescription("UPS Ground");
		grnd.setServiceLevel("GRND");
		ShippingRate shippingRate1 = createShippingRate(10d, grnd);

		ShippingLevel twoDay = new ShippingLevel();
		twoDay.setDescription("UPS 2-Day Saver");
		twoDay.setServiceLevel("2DAY");
		ShippingRate shippingRate2 = createShippingRate(25d, twoDay);

		List<ShippingRate> allRates = new ListBuilder<ShippingRate>().add(shippingRate2).add(shippingRate1).getList();

		Item item1 = (Item) session.get(Item.class, 1L);
		Item item2 = (Item) session.get(Item.class, 2L);

		Pricing pricing = new Pricing();
		pricing.setPrice(10.0);
		pricing.setMinimum(1);
		pricing.setMultiplier(1);

		EasyMock.expect(mockItemService.retireveCurrentPricing(item1, null)).andReturn(pricing).anyTimes();
		EasyMock.expect(mockItemService.retireveCurrentPricing(item2, null)).andReturn(pricing).anyTimes();
		EasyMock.expect(mockTaxService.calculateTax(null)).andReturn(BigDecimal.ZERO).anyTimes();
		EasyMock.expect(mockShippingService.lookupShippingOptions(null, null, null, null)).andReturn(allRates).anyTimes();
		EasyMockUnitils.replay();

		cartService.add(item1, new Integer(10));

		cartService.add(item2, new Integer(5));

		Address shippingAddress = new Address();
		cartService.setShippingAddress(shippingAddress);

		Assert.assertEquals(new Double(10), cartService.getShipping());
		Assert.assertEquals("GRND", cartService.getShippingLevel().getServiceLevel());

		// Set new shippingLevel and everything should recalculate
		cartService.setShippingLevel(twoDay);

		Assert.assertEquals(new Double(25), cartService.getShipping());
		Assert.assertEquals("2DAY", cartService.getShippingLevel().getServiceLevel());

		EasyMockUnitils.verify();

	}

	@Test
	public void testAddWithBadMinimum() {
		Session session = sessionFactory.getCurrentSession();

		Item item1 = (Item) session.get(Item.class, 1l);
		Item item2 = (Item) session.get(Item.class, 2l);

		Pricing pricing = new Pricing();
		pricing.setPrice(10.0);
		pricing.setMinimum(10);
		pricing.setMultiplier(10);

		// called during calcuate
		EasyMock.expect(mockItemService.retireveCurrentPricing(item1, null)).andReturn(pricing).anyTimes();
		EasyMock.expect(mockItemService.retireveCurrentPricing(item2, null)).andReturn(pricing).anyTimes();

		EasyMock.expect(mockTaxService.calculateTax(null)).andReturn(BigDecimal.ZERO).anyTimes();
		EasyMockUnitils.replay();

		// Adding item to cart
		try {
			cartService.add(item1, new Integer(5));
			Assert.fail();
		} catch (Exception e) {
		}

		Assert.assertEquals(0, cartService.getItemCount());
		Assert.assertEquals(0.0, cartService.getSubtotal());

		EasyMockUnitils.verify();
	}

	@Test
	public void testAddWithZeroQuantity() {
		Session session = sessionFactory.getCurrentSession();

		Item item1 = (Item) session.get(Item.class, 1l);

		// called during calcuate
		EasyMock.expect(mockTaxService.calculateTax(null)).andReturn(BigDecimal.ZERO).anyTimes();
		EasyMockUnitils.replay();

		cartService.add(item1, new Integer(0));

		Assert.assertEquals(0, cartService.getItemCount());
		Assert.assertEquals(new Double(0), cartService.getSubtotal());

		EasyMockUnitils.verify();
	}

	
	@Test
	public void testAdd() {
		Session session = sessionFactory.getCurrentSession();

		Item item1 = (Item) session.get(Item.class, 1l);
		Item item2 = (Item) session.get(Item.class, 2l);

		Pricing pricing = new Pricing();
		pricing.setPrice(10.0);
		pricing.setMinimum(1);
		pricing.setMultiplier(1);

		// called during calcuate
		EasyMock.expect(mockItemService.retireveCurrentPricing(item1, null)).andReturn(pricing).anyTimes();
		EasyMock.expect(mockItemService.retireveCurrentPricing(item2, null)).andReturn(pricing).anyTimes();

		EasyMock.expect(mockTaxService.calculateTax(null)).andReturn(BigDecimal.ZERO).anyTimes();
		EasyMockUnitils.replay();

		// Adding item to cart
		cartService.add(item1, new Integer(5));

		Assert.assertEquals(1, cartService.getItemCount());
		Assert.assertEquals(new Double(50), cartService.getSubtotal());

		// Add existing item to cartsecond
		cartService.add(item1, new Integer(10));

		Assert.assertEquals(1, cartService.getItemCount());
		Assert.assertEquals(new Double(100), cartService.getSubtotal());

		// Adding item again with quantity 0, should remove
		cartService.add(item1, new Integer(0));

		Assert.assertEquals(0, cartService.getItemCount());
		Assert.assertEquals(new Double(0), cartService.getSubtotal());

		// Adding new item with quantity 0, should have no effect
		cartService.add(item2, new Integer(0));

		Assert.assertEquals(0, cartService.getItemCount());
		Assert.assertEquals(new Double(0), cartService.getSubtotal());

		EasyMockUnitils.verify();
	}

	// @Test
	// public void testPlaceOrder() throws Exception {
	//
	// Session currentSession = sessionFactory.getCurrentSession();
	//
	// ShippingLevel shippingLevel = (ShippingLevel)
	// currentSession.get(ShippingLevel.class, 1L);
	//
	// Item item = (Item) currentSession.get(Item.class, 1L);
	//
	// Account account = (Account) currentSession.get(Account.class, 1L);
	//
	// Address shippingAddress = new Address();
	// shippingAddress.setName("shippingAddress");
	//		
	// Address billingAddress = new Address();
	// billingAddress.setName("billingAddress");
	//
	// cartService.add(item, item.getCurrentPrice(account.getType()).getPrice(),
	// 4);
	// cartService.setShippingAddress(shippingAddress);
	// cartService.setBillingAddress(billingAddress);
	//		
	// Payment payment = new Payment();
	// payment.setAccount("4111111111111111");
	// payment.setExpires("0212");
	// payment.setStatus(Payment.NEW);
	// payment.setType("MC");
	// payment.setAmount(cartService.getTotal());
	// payment.setCvn("000");
	//
	// ShippingRate shippingRate1 = createShippingRate(10d, shippingLevel);
	//
	// EasyMock.expect(mockTaxService.calculateTax(new Double(40),
	// null)).andReturn(BigDecimal.ZERO);
	// EasyMock.expect(mockTaxService.calculateTax(new Double(40),
	// null)).andReturn(new BigDecimal(5));
	// EasyMock.expect(mockShippingService.lookupShippingOptions(shippingAddress,
	// null, new Double(40))).andReturn(
	// ListBuilder.newInstance().add(shippingRate1).getList());
	// mockPaymnetService.processAuthorization(payment);
	// EasyMockUnitils.replay();
	//
	//
	//
	// Order order = cartService.placeOrder(account, payment);
	//
	// Assert.assertEquals("Wrong shipping charges", new Double(10),
	// order.getShipping());
	// Assert.assertEquals("Wrong subtotal", new Double(40.0),
	// order.getSubtotal());
	// Assert.assertEquals("Wrong tax", new Double(5.00), order.getTax());
	// Assert.assertEquals("Wrong total", new Double(55.00), order.getTotal());
	// Assert.assertEquals("Wrong shipping level", "GRND",
	// order.getShippingLevel().getServiceLevel());
	// Assert.assertEquals("Wrong status", "New", order.getStatus());
	// Assert.assertEquals("Wrong item count", 1, cartService.getItemCount());
	// Assert.assertEquals("Wrong discount", new Double(0),
	// cartService.getDiscount());
	// Assert.assertFalse("Cart should not be empty", cartService.isEmpty());
	// Assert.assertFalse("Order is not submitted", cartService.isSubmitted());
	// Assert.assertEquals(Payment.NEW, payment.getStatus());
	//
	// EasyMockUnitils.verify();
	// }

	private ShippingRate createShippingRate(final Double amt, final ShippingLevel shippingLevel) {
		ShippingRate shippingRate = new ShippingRate(shippingLevel, amt);
		return shippingRate;
	}

}
