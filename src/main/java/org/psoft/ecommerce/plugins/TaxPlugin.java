package org.psoft.ecommerce.plugins;

import java.math.BigDecimal;

import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.service.CartService;

public interface TaxPlugin extends Plugin {

	public BigDecimal calculateTax(CartService cartService);

	public Double getTaxRate(Address address);

}