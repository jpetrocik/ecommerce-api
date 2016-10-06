package org.psoft.ecommerce.data;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Pricing;
import org.unitils.UnitilsJUnit4;

public class TestPricing extends UnitilsJUnit4 {

	private static long DAY = 86400000;

	@Test
	public void testIsActive() {
		Date today = new Date();
		Date startDate = new Date(today.getTime() - DAY);
		Date endDate = new Date(today.getTime() + DAY);

		Pricing pricing = new Pricing();
		pricing.setStartDate(startDate);
		pricing.setEndDate(endDate);

		Assert.assertTrue(pricing.isActive());

	}

	@Test
	public void testIsActiveNullEndDate() {
		Date startDate = new Date();
		startDate.setTime(startDate.getTime() - DAY);
		Pricing pricing = new Pricing();
		pricing.setStartDate(startDate);

		Assert.assertTrue(pricing.isActive());

	}

	@Test
	public void testIsActiveFutureStartDate() {
		Date startDate = new Date();
		startDate.setTime(startDate.getTime() + DAY);
		Pricing pricing = new Pricing();
		pricing.setStartDate(startDate);

		Assert.assertFalse(pricing.isActive());

	}

	@Test
	public void testIsActivePassedEndDate() {
		Date today = new Date();
		Date startDate = new Date(today.getTime() - (2 * DAY));
		Date endDate = new Date(today.getTime() - DAY);

		Pricing pricing = new Pricing();
		pricing.setStartDate(startDate);
		pricing.setEndDate(endDate);

		Assert.assertFalse(pricing.isActive());

	}

}
