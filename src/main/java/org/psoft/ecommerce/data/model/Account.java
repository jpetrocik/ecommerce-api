package org.psoft.ecommerce.data.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.BooleanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 
 * Account is the user identifier.  Users create accounts to store order history and to associate pricing history.
 * 
 */
@Entity
@Data
public class Account {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	Date createdOn = new Date();

	@Column(nullable=false)
	String email;

	@JsonIgnore
	@Column(nullable=false, columnDefinition = "TEXT")
	String password;
	
	@JsonIgnore
	@Column(nullable=false, columnDefinition = "TEXT")
	String passsalt;

	@ManyToOne
	@JoinColumn(name="account_type_id", nullable=false)
	AccountType accountType;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="shipping_address_id", nullable=true)
	Address shippingAddress;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="billing_address_id", nullable=true)
	Address billingAddress;

	@Column(nullable=false)
	Boolean suspended = Boolean.FALSE;
	
	String resetToken;

}
