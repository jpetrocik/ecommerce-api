package org.psoft.ecommerce.rest;

import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.repo.AccountRepo.AccountExistsException;
import org.psoft.ecommerce.data.repo.AccountRepo.NoAccountExistsException;
import org.psoft.ecommerce.rest.view.AccountView;
import org.psoft.ecommerce.rest.view.AddressView;
import org.psoft.ecommerce.service.AccountService;
import org.psoft.ecommerce.service.AccountService.AccountResetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountRestController {

	@Autowired
	protected AccountService accountService;

	@RequestMapping(method=RequestMethod.GET, value="/create")
	public AccountView createAccount(@RequestParam String email, @RequestParam String password, 
			@RequestParam String accountType) throws AccountExistsException {
		
		Account account = accountService.createAccount(email, password, accountType);
		return AccountView.create(account);
	}
	
	@RequestMapping("/defaultShippingAddress")
	public AddressView setDefaultShippingAddress(@RequestParam Long accountId, @RequestParam String name, 
			@RequestParam(required = false) String company, @RequestParam String address1, 
			@RequestParam(required = false) String address2, @RequestParam String city, @RequestParam String state, 
			@RequestParam String postalCode, @RequestParam(required = false) String phone, 
			@RequestParam(required = false) String altPhone) throws NoAccountExistsException {

			Address address = accountService.setDefaultShippingAddress(accountId, name, company, address1, address2, 
					city, state, postalCode, phone, altPhone);
			return AddressView.create(address);
	}

	@RequestMapping("/defaultBillingAddress")
	public AddressView setDefaultBillingAddress(@RequestParam Long accountId, @RequestParam String name, 
			@RequestParam(required = false) String company, @RequestParam String address1, 
			@RequestParam(required = false) String address2, @RequestParam String city, @RequestParam String state, 
			@RequestParam String postalCode, @RequestParam(required = false) String phone, 
			@RequestParam(required = false) String altPhone) throws NoAccountExistsException {

			Address address = accountService.setDefaultBillingAddress(accountId, name, company, address1, address2, 
					city, state, postalCode, phone, altPhone);
			return AddressView.create(address);
	}

	@RequestMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email) {
		return accountService.generateResetPasswordToken(email);
	}

	@RequestMapping("/resetPassword")
	public void resetPassword(@RequestParam String email, @RequestParam String password, @RequestParam String token) throws AccountResetException {
		accountService.resetPassword(email, password, token);
	}

	@RequestMapping("/changePassword")
	public void changePassword(@RequestParam String email, @RequestParam String oldPassword, @RequestParam String newPassword) throws AccountResetException, NoAccountExistsException {
		accountService.changePassword(email, oldPassword, newPassword);
	}

	@RequestMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password) throws NoAccountExistsException {
		return accountService.verifyAccount(email, password);
	}

}
