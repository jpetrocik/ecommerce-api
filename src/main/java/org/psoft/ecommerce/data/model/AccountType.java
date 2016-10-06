package org.psoft.ecommerce.data.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;

/**
 * AccountType is used for associating different pricing for users.  An AccountType has pricing levels associated with it.  The lowest price
 * is selected to display to the user.  Also AccountType is used to determine is tax is calculated.  
 *  
 * @hibernate.class table="account_type"
 * 
 * @author jpetrocik
 * 
 */
@Entity
@Data
public class AccountType {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false)
	String name;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="account_type_to_pricing_level")
	Set<PricingLevel> pricingLevels = new HashSet<PricingLevel>();

	public Set<PricingLevel> getPricingLevels() {
		return Collections.unmodifiableSet(pricingLevels);
	}

	public void addPricingLevel(PricingLevel pricingLevel) {
		pricingLevels.add(pricingLevel);
	}

	public void removePricingLevel(PricingLevel pricingLevel) {
		pricingLevels.remove(pricingLevel);
	}


}
