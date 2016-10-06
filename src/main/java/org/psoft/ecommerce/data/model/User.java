package org.psoft.ecommerce.data.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @hibernate.class table="cc_user"
 * 
 * @author jpetrocik
 * 
 */
public class User {

	Long id;
	
	String fullName;
	
	String username;
	
	String password;
	
	Set<Group> groups = new HashSet<Group>();

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
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @hibernate.property not-null="true"
	 */
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @hibernate.property
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @hibernate.set table="cc_user_to_group" access="field" lazy="true"
	 * @hibernate.key column="user_id"
	 * @hibernate.many-to-many class="org.psoft.ecommerce.data.Group"
	 *                         column="group_id"
	 * 
	 * @return
	 */
	public Set<Group> getGroups() {
		return Collections.unmodifiableSet(groups);
	}
	
	public void addGroup(Group group){
		groups.add(group);
	}
	
	public void removeGroup(Group group){
		groups.remove(group);
	}

}
