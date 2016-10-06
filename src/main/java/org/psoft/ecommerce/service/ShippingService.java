package org.psoft.ecommerce.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.ShippingLevel;
import org.psoft.ecommerce.data.model.ShippingRate;
import org.psoft.ecommerce.data.model.State;
import org.psoft.ecommerce.plugins.ShippingPlugin;

public class ShippingService extends AbstractService {
	private static Log log = LogFactory.getLog(ShippingService.class);

	@SuppressWarnings("unchecked")
	public List<ShippingLevel> allShippingLevels() {
		List<ShippingLevel> shippingLevels = (List<ShippingLevel>) currentSession().createCriteria(ShippingLevel.class).list();

		return shippingLevels;
	}

	@SuppressWarnings("unchecked")
	public List<State> shipableRegions() {
		List<State> states = currentSession().createCriteria(State.class).list();

		return states;
	}

	public List<ShippingRate> lookupShippingOptions(Account account, Address address, Integer weight, Double value) {
		ShippingPlugin shippingPlugin = (ShippingPlugin) lookupPlugin(ShippingPlugin.class);

		return shippingPlugin.lookupShippingOptions(account, address, weight, value);

	}

	public ShippingRate calculateShippingRate(String level, Account account,  Address address, Integer weight, Double value) {
		ShippingPlugin shippingPlugin = (ShippingPlugin) lookupPlugin(ShippingPlugin.class);

		return shippingPlugin.calculateShippingRate(level, account, address, weight, value);
	}

}
