package org.psoft.ecommerce.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Address {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false)
	String name;

	String company;

	@Column(nullable = false)
	String address1;

	String address2;

	@Column(nullable = false)
	String city;

	@Column(nullable = false)
	String state;

	@Column(nullable = false)
	String postalCode;

	String phone;

	String altPhone;

}
