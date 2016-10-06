package org.psoft.ecommerce.data.model;

/**
 * @hibernate.class table="shipping_level"
 * 
 * @author jpetrocik
 * 
 */
public class ShippingLevel {

	private Long id;

	private String serviceLevel;

	private String description;

	/**
	 * @hibernate.id column="id" generator-class="native"
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return description;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ShippingLevel) {
			ShippingLevel sl = (ShippingLevel) obj;

			return serviceLevel.equals(sl.getServiceLevel());
		}
		return false;
	}
}
