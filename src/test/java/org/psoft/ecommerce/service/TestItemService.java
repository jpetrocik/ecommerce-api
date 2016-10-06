package org.psoft.ecommerce.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Pricing;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestItemService extends UnitilsJUnit4 {

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	ItemService itemService;

	@Before
	public void setUp() {
		itemService = new ItemService();
		itemService.setSessionFactory(sessionFactory);
	}

	@Test
	public void testRetireveCurrentPricingNullAccount() throws Exception {
		Session session = sessionFactory.getCurrentSession();

		Item item = (Item) session.get(Item.class, 1L);

		Pricing price = itemService.retireveCurrentPricing(item, null);

		Assert.assertEquals(new Double(10.35), price.getPrice());
		Assert.assertEquals("Default", price.getPricingLevel().getName());

	}

	@Test
	public void testRetireveCurrentPricing() throws Exception {
		Session session = sessionFactory.getCurrentSession();

		Item item = (Item) session.get(Item.class, 1L);
		AccountType accountType = (AccountType) session.get(AccountType.class, 2L);

		Pricing price = itemService.retireveCurrentPricing(item, accountType);

		Assert.assertEquals(new Double(8.50), price.getPrice());
		Assert.assertEquals("Pro", price.getPricingLevel().getName());

	}

}
