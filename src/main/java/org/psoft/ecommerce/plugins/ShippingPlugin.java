package org.psoft.ecommerce.plugins;

import java.util.List;

import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.ShippingRate;

public interface ShippingPlugin extends Plugin {

	public List<ShippingRate> lookupShippingOptions(Account account, Address address, Integer weight, Double amount);

	public ShippingRate calculateShippingRate(String level, Account account, Address address, Integer weight, Double value);

}
