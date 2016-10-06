package org.psoft.ecommerce.data.model;

/**
 * @hibernate.class table="state"
 * 
 * @author jpetrocik
 * 
 */
public class State {

	public String abbr;

	public String name;

	/**
	 * @hibernate.id column="abbr" generator-class="native"
	 */
	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
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

	public int compareTo(Object o) {
		State state = (State) o;
		return getName().compareTo(state.getName());
	}

}
