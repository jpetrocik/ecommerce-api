package org.psoft.ecommerce.service;

import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.PricingLevel;
import org.psoft.ecommerce.data.repo.AccountTypeRepo;
import org.psoft.ecommerce.data.repo.PricingLevelRepo;
import org.psoft.ecommerce.data.repo.PricingLevelRepo.NoPricingLevelExists;
import org.psoft.ecommerce.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Account types are used for different pricing level and checkout rules.  Different pricing levels
 * for distributors vs wholesalers or pro pricing for sponsored athletes.  Also different rules can be applied
 * during checkout.
 *
 */
@Service
public class AccountTypeService extends AbstractService<AccountType> {

	@Autowired
	AccountTypeRepo accountTypeRepo;
	
	@Autowired
	PricingLevelRepo pricingLevelRepo;

	public void createAccountType(String name) {
		AccountType accountType = new AccountType();
		accountType.setName(Assert.value(name, "Account type name is required"));

		accountTypeRepo.save(accountType);
	}

	public AccountType byId(Long accountTypeId) {
		return accountTypeRepo.findOne(accountTypeId);
	}

	public void addAccountTypePricingLevel(Long accountTypeId, Long pricingLevelId) {
		AccountType accountType = byId(accountTypeId);
		Assert.notNull(accountType, RuntimeException.class, "No account type exist for " + accountTypeId);
		
		PricingLevel pricingLevel = pricingLevelRepo.findOne(pricingLevelId);
		Assert.notNull(pricingLevel, NoPricingLevelExists.class, pricingLevelId);

		accountType.addPricingLevel(pricingLevel);

		accountTypeRepo.save(accountType);
	}

	public void removeAccountTypePricingLevel(Long accountTypeId, Long pricingLevelId) {
		AccountType accountType = byId(accountTypeId);
		Assert.notNull(accountType, RuntimeException.class, "No account type exist for " + accountTypeId);
		
		PricingLevel pricingLevel = pricingLevelRepo.findOne(pricingLevelId);
		Assert.notNull(pricingLevel, NoPricingLevelExists.class, pricingLevelId);


		accountType.removePricingLevel(pricingLevel);

		accountTypeRepo.save(accountType);
	}
	
}
