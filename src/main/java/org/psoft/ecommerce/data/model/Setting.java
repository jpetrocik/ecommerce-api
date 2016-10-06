package org.psoft.ecommerce.data.model;

/**
 * @hibernate.class table="settings"
 * 
 * @author jpetrocik
 * 
 */
public class Setting {

	private String name;

	private String value;

	/**
	 * @hibernate.id column="name" generator-class ="assigned"
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
