package org.psoft.ecommerce.service.client.data;

import lombok.Data;


@Data
public class AccountClientView {

	String accountType;
	
	String email;
	
	String billingName;
	
	String billingCompany;
	
	String billingAddress1;
	
	String billingAddress2;
	
	String billingCity;
	
	String billingState;
	
	String billingPostalCode;
	
	String billingPhone;
	
	String billingAltPhone;

	String shippingName;
	
	String shippingCompany;
	
	String shippingAddress1;
	
	String shippingAddress2;
	
	String shippingCity;
	
	String shippingState;
	
	String shippingPostalCode;
	
	String shippingPhone;
	
	String shippingAltPhone;

	Long id;
}
