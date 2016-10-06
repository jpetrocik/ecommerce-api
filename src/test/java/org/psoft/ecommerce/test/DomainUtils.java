package org.psoft.ecommerce.test;

import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Product;

public class DomainUtils {

	public static AccountType createAccountType(){
		AccountType type = new AccountType();
		
		type.setName("Consumer");
		type.setIsDefault(Boolean.TRUE);
		
		return type;
	}
	
	public static Account createAccount(String email, String password) {
		Account account = new Account();
		account.setEmail(email);
		account.setPassword(password);
		
		return account;
	}
	
	public static Address createAddress(){
		return DomainUtils.createAddress("John Doe", "111 Nowhere St.", "Long Beach", "CA", "90815");
	}

	public static Address createAddress(String name, String address1, String city, String state, String postal) {
		Address address = new Address();
		address.setName(name);
		address.setAddress1(address1);
		address.setCity(city);
		address.setState(state);
		address.setPostalCode(postal);

		return address;
	}
	
	public static Category createCategory(String title){
		Category category = new Category();
		category.setIsActive(Boolean.TRUE);
		category.setTitle(title);
		
		return category;
	}

	public static Product createProduct(String title){
		Product product = new Product();
		product.setIsActive(Boolean.TRUE);
		product.setTitle(title);
		product.setCode(title);
		
		return product;
	}

	public static Item createItem(String sku, String title) {
		Item item = new Item();
		item.setPartNum(sku);
		item.setTitle(title);
		item.setIsActive(Boolean.TRUE);
		return item;
	}
}
