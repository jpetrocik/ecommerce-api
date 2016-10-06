package org.psoft.ecommerce.service;

import java.math.BigDecimal;

import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.plugins.TaxPlugin;

public class TaxService extends AbstractService {

	public BigDecimal calculateTax(CartService cartService){

		TaxPlugin taxPlugin = (TaxPlugin) lookupPlugin(TaxPlugin.class);

		return taxPlugin.calculateTax(cartService);
	}

	public Double getTaxRate(Address address) {

		TaxPlugin taxPlugin = (TaxPlugin) lookupPlugin(TaxPlugin.class);

		return taxPlugin.getTaxRate(address);
	}

}
