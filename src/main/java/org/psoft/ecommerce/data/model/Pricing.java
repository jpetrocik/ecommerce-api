package org.psoft.ecommerce.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Pricing implements Comparable<Pricing> {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false)
	Date endDate;

	Date startDate = new Date();

	@Column(nullable = false)
	Double price;

	@Column(nullable = false)
	Integer minimum;
	
	@Column(nullable = false)
	Integer multiplier;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	PricingLevel pricingLevel;

	public boolean isActive() {
		boolean isActive = true;
		Date today = new Date();

		isActive = today.after(startDate) ? isActive : false;

		if (endDate != null)
			isActive = today.before(endDate) ? isActive : false;

		return isActive;
	}

	/**
	 * Because Pricing is in a set in item, comparable interface is required.
	 * This implementation simply compare startDate for lack of something
	 * better.
	 */
	public int compareTo(Pricing o) {
		return startDate.compareTo(o.getStartDate());
	}

}
