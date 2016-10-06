package org.psoft.ecommerce.admin.rest;

import org.psoft.ecommerce.rest.AccountRestController;
import org.psoft.ecommerce.rest.view.AccountView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/account")
public class AdminAccountRestController extends AccountRestController {

	@RequestMapping("/find")
	@ResponseBody
	public AccountView findAccount(@RequestParam String email){
		return AccountView.create(accountService.byEmail(email));
	}

	@RequestMapping("/get")
	@ResponseBody
	public AccountView findAccount(@RequestParam Long accountId){
		return AccountView.create(accountService.byId(accountId));
	}
	
	@RequestMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email) {
		return accountService.generateResetPasswordToken(email);
	}

}
