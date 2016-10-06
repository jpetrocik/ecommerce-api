package org.psoft.ecommerce.service;

import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Pricing;

public class InvalidOrderQty extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4310462161146596067L;

	Pricing pricing;
	
	Integer qty;
	
	Item item;
	
	public InvalidOrderQty(Item item, Pricing pricing, Integer qty) {
		this.pricing=pricing;
		this.qty=qty;
		this.item=item;
	}

	
	public Pricing getPricing() {
		return pricing;
	}


	public void setPricing(Pricing pricing) {
		this.pricing = pricing;
	}


	public Integer getQty() {
		return qty;
	}


	public void setQty(Integer qty) {
		this.qty = qty;
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}


	@Override
	public String getMessage() {
		return "Quantity ordered (" + qty + ") must be greater then " + pricing.getMinimum() + " and a multiple of " + pricing.getMultiplier();
	}


	
	
	
}
