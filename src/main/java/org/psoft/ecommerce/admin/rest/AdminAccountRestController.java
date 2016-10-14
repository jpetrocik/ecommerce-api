package org.psoft.ecommerce.admin.rest;

import org.psoft.ecommerce.rest.AccountRestController;
import org.psoft.ecommerce.rest.view.AccountView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/account")
public class AdminAccountRestController extends AccountRestController {

	@RequestMapping(path = "/", method = { RequestMethod.GET })
	public AccountView findAccount(@RequestParam String email) {
		return AccountView.create(accountService.byEmail(email));
	}

	@RequestMapping(path = "/{accountId}", method = { RequestMethod.GET })
	public AccountView accountById(@PathVariable Long accountId) {
		return AccountView.create(accountService.byId(accountId));
	}

	@RequestMapping(path = "/forgotPassword", method = { RequestMethod.POST })
	public String forgotPassword(@RequestParam String email) {
		return accountService.generateResetPasswordToken(email);
	}

}
