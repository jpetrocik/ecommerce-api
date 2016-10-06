package org.psoft.ecommerce.plugins.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.ShippingLevel;
import org.psoft.ecommerce.data.model.ShippingRate;
import org.psoft.ecommerce.plugins.AbstractPlugin;
import org.psoft.ecommerce.plugins.ShippingPlugin;

public class FlatRateShippingPlugin extends AbstractPlugin implements ShippingPlugin {

	private static String RATE_SETTING = "flatrate_shipping_rate";

	public String getName() {
		return "Flat Rate Shipping";
	}

	public Map<String, Object> getSettings() {
		Map<String, Object> settings = new HashMap<String, Object>();

		settings.put(RATE_SETTING, getApplicationSettings().getDouble(FlatRateShippingPlugin.RATE_SETTING, 10d));

		return settings;
	}

	public List<ShippingRate> lookupShippingOptions(Account account, Address address, Integer weight, Double amount) {

		List<ShippingRate> rates = new ArrayList<ShippingRate>();

		ShippingLevel shippingLevel = (ShippingLevel) currentSession().get(ShippingLevel.class, 1L);

		Double rate = getApplicationSettings().getDouble(FlatRateShippingPlugin.RATE_SETTING, 10d);

		ShippingRate flatRate = new ShippingRate(shippingLevel, rate);

		rates.add(flatRate);

		return rates;
	}

	public ShippingRate calculateShippingRate(String level, Account account, Address address, Integer weight, Double value) {
		ShippingLevel shippingLevel = (ShippingLevel) currentSession().get(ShippingLevel.class, 1L);

		Double rate = getApplicationSettings().getDouble(FlatRateShippingPlugin.RATE_SETTING, 10d);

		ShippingRate flatRate = new ShippingRate(shippingLevel, rate);

		return flatRate;
	}

}
