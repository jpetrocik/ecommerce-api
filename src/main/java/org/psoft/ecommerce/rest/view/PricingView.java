package org.psoft.ecommerce.rest.view;

import java.text.SimpleDateFormat;

import org.psoft.ecommerce.data.model.Pricing;

public class PricingView {

	Pricing pricing;

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static PricingView create(Pricing pricing) {
		return new PricingView(pricing);
	}

	private PricingView(Pricing pricing) {
		this.pricing = pricing;
	}

	public String getEndDate() {

		if (pricing.getEndDate() == null)
			return "";

		return dateFormat.format(pricing.getEndDate());
	}

	public Long getId() {
		return pricing.getId();
	}

	public Double getPrice() {
		return pricing.getPrice();
	}

	public String getPricingLevel() {
		return pricing.getPricingLevel().getName();
	}

	public String getStartDate() {
		return dateFormat.format(pricing.getStartDate());
	}

	public boolean getIsActive() {
		return isActive();
	}

	public boolean isActive() {
		return pricing.isActive();
	}

	public Integer getMinimum() {
		return pricing.getMinimum();
	}

	public Integer getMultiplier() {
		return pricing.getMultiplier();
	}

}
