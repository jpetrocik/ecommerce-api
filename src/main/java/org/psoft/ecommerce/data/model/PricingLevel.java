package org.psoft.ecommerce.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * Represents the pricing level, so logic can be built around pricing.
 * 
 * @hibernate.class table="pricing_level"
 * 
 * @author jpetrocik
 * 
 */
@Entity
@Data
public class PricingLevel {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable=false)
	String name;

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof PricingLevel) {
			PricingLevel pl = (PricingLevel) obj;

			return pl.getName().equals(name);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
