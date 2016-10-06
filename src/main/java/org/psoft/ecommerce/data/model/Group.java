package org.psoft.ecommerce.data.model;

/**
 * @hibernate.class table="cc_group"
 * 
 * @author jpetrocik
 * 
 */
public class Group {

	Long id;
	
	String name;

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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
