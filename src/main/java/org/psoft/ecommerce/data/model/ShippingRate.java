package org.psoft.ecommerce.data.model;

/**
 * 
 * @author jpetrocik
 * 
 */
public class ShippingRate {

	ShippingLevel shippingLevel;

	Double rate;

	public ShippingRate(ShippingLevel shippingLevel, Double rate) {
		this.shippingLevel = shippingLevel;
		this.rate = rate;
	}

	public ShippingLevel getShippingLevel() {
		return shippingLevel;
	}

	public Double getRate() {
		return rate;
	}

}
