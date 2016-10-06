package org.psoft.ecommerce.data;

import java.util.Date;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Pricing;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestItem extends UnitilsJUnit4 {

	private static long DAY = 86400000;

	@Test
	public void testGetPricingActive() {
		Item item = new Item();
		Pricing expired = createPricing(DAY * 2, -DAY, 10.00);
		Pricing current = createPricing(DAY, DAY, 10.00);
		Pricing future = createPricing(-DAY, (DAY * 2), 10.00);
		item.addPricing(expired);
		item.addPricing(current);
		item.addPricing(future);

		Set<Pricing> activePricing = item.getActivePricing();
		Assert.assertEquals(1, activePricing.size());

		Set<Pricing> pricing = item.getPricing();
		Assert.assertEquals(3, pricing.size());
	}

	private Pricing createPricing(long start, long end, double price) {
		Date today = new Date();
		Date startDate = new Date(today.getTime() - start);
		Date endDate = new Date(today.getTime() + end);

		Pricing pricing = new Pricing();
		pricing.setStartDate(startDate);
		pricing.setEndDate(endDate);
		pricing.setPrice(price);

		return pricing;

	}

}
