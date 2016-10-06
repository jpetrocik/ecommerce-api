package org.psoft.ecommerce.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.repo.AccountRepo;
import org.psoft.ecommerce.data.repo.AccountRepo.AccountExistsException;
import org.psoft.ecommerce.data.repo.AccountRepo.NoAccountExistsException;
import org.psoft.ecommerce.data.repo.AccountTypeRepo;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.psoft.ecommerce.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Manages user account.
 */
@Service
public class AccountService extends AbstractService<Account> {
	private static final Log log = LogFactory.getLog(AccountService.class);

	@Autowired
	AccountRepo accountRepo;
	
	@Autowired
	AccountTypeRepo accountTypeRepo;
	
	/**
	 * Retrieves an account by id
	 */
	public Account byId(Long accountId) {
		Account account = accountRepo.findOne(accountId);

		return account;

	}

	/**
	 * Authenicates the user and returns a session token
	 */
	public String verifyAccount(String email, String password) {
		Account account = accountRepo.findByEmail(email);
		if (account == null) {
			throw new NoAccountExistsException("Authenicated failed");
		}
		
		byte[] salt;
		try {
			salt = Hex.decodeHex(account.getPasssalt().toCharArray());
		} catch (DecoderException e) {
			throw new NoAccountExistsException("Authenicated failed");
		}
		
		String mungedPassword = StringUtils.trim(password + "F3Se532KGF=");
			
		MessageDigest digest = DigestUtils.getSha512Digest();
		digest.update(salt);
		digest.update(mungedPassword.getBytes());
		byte[] hashedPassword = digest.digest();
			
		if (!account.getPassword().equals(Hex.encodeHexString(hashedPassword))){
			throw new NoAccountExistsException("Authenicated failed");
		}

		byte[] seed = SecureRandom.getSeed(20);
		String token = DigestUtils.sha1Hex(seed.toString());
		return token;
	}

	/**
	 * Returns all the available account types
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AccountType> allAccountTypes() {
		List<AccountType> accountTypes = new ArrayList<>();

		Iterable<AccountType> accountTypeIterable = accountTypeRepo.findAll();
		for (AccountType a : accountTypeIterable){
			accountTypes.add(a);
		}		
		
		return accountTypes;
	}

	/**
	 * Looks up the account by email
	 */
	public Account byEmail(String email) {
		Account account = accountRepo.findByEmail(StringUtils.trimToEmpty(email));

		return account;
	}

	/**
	 * Searches account by name, email, phone. This performs a case insensitive
	 * like. Wild cards are not implied, they must be explicit in the parameters
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Account> searchAccount(String name, String email, String phone) {
		throw new NotImplementedException("Implement account search");
//		Session session = currentSession();
//
//		log.debug("Search account (" + name + " , " + email + " , " + phone + ")");
//
//		Criteria criteria = session.createCriteria(Account.class);
//
//		if (StringUtils.isNotBlank(name))
//			criteria.createCriteria("billingAddress").add(Restrictions.ilike("name", name));
//
//		if (StringUtils.isNotBlank(email))
//			criteria.add(Restrictions.ilike("email", email));
//
//		if (StringUtils.isNotBlank(phone))
//			criteria.createCriteria("billingAddress").add(Restrictions.ilike("phone", phone));
//
//		return (List<Account>) criteria.list();
	}

	/**
	 * Creates a new account if one does not already exists.

	 * @param accountType An account type allows for different pricing and checkout rules, e.g wholesaler, pro pricing, distributor 
	 */
	public Account createAccount(String email, String password, String accountTypeName) {

		// verify account does not exist
		if (byEmail(email) != null) {
			throw new AccountExistsException("An account with the email " + email + " all ready exist");
		}

		// verify account type exist
		AccountType accountType = null;
		if (accountTypeName != null) {
			accountType = accountTypeRepo.findByName(accountTypeName);
		}
		
		//TODO Implement accountType fetch by isDefault
//		if (type == null) {
//			type = (AccountType) get(session.createCriteria(AccountType.class).add(Restrictions.eq("isDefault", true)));
//		}
		Validate.notNull(accountType, "Unabel to find account type " + accountType);
		
		Account account = new Account();
		account.setEmail(StringUtils.trimToNull(email));
		updateAccountPassword(account, password);
		account.setAccountType(accountType);

		return accountRepo.save(account);
	}
	
	private void updateAccountPassword(Account account, String password) {
		if (StringUtils.isBlank(password)){
			throw new RuntimeException("Passwod can not be blank");
		}
		
		String mungedPassword = StringUtils.trim(password + "F3Se532KGF=");
		byte[] salt = SecureRandom.getSeed(256);
		
		MessageDigest digest = DigestUtils.getSha512Digest();
		digest.update(salt);
		digest.update(mungedPassword.getBytes());
		byte[] hashedPassword = digest.digest();
		
		account.setPassword(Hex.encodeHexString(hashedPassword));
		account.setPasssalt(Hex.encodeHexString(salt));
		account.setResetToken(null);
	}

	public Address setDefaultShippingAddress(Long accountId, String name, String company, String address1, String address2, 
			String city, String state, String postalCode, String phone, String altPhone) {
	
		Account account = byId(accountId);
		if (account == null) {
			throw new NoAccountExistsException("No account for " + accountId);
		}

		Address shippingAddress = account.getShippingAddress();
		if (shippingAddress == null) {
			shippingAddress = new Address();
		}
			
		shippingAddress.setName(StringUtils.trimToNull(name));
		shippingAddress.setCompany(StringUtils.trimToNull(company));
		shippingAddress.setAddress1(StringUtils.trimToNull(address1));
		shippingAddress.setAddress2(StringUtils.trimToNull(address2));
		shippingAddress.setCity(StringUtils.trimToNull(city));
		shippingAddress.setState(StringUtils.trimToNull(state));
		shippingAddress.setPostalCode(StringUtils.trimToNull(postalCode));
		shippingAddress.setPhone(StringUtils.trimToNull(phone));
		shippingAddress.setAltPhone(StringUtils.trimToNull(altPhone));
		
		account.setShippingAddress(shippingAddress);

		account = accountRepo.save(account);
		
		return account.getShippingAddress();
	}

	public Address setDefaultBillingAddress(Long accountId, String name, String company, String address1, String address2, 
			String city, String state, String postalCode, String phone, String altPhone) {
		
		Account account = byId(accountId);
		if (account == null) {
			throw new NoAccountExistsException("No account for " + accountId);
		}

		Address billingAddress = account.getBillingAddress();
		if (billingAddress == null) {
			billingAddress = new Address();
		}
			
		billingAddress.setName(StringUtils.trimToNull(name));
		billingAddress.setCompany(StringUtils.trimToNull(company));
		billingAddress.setAddress1(StringUtils.trimToNull(address1));
		billingAddress.setAddress2(StringUtils.trimToNull(address2));
		billingAddress.setCity(StringUtils.trimToNull(city));
		billingAddress.setState(StringUtils.trimToNull(state));
		billingAddress.setPostalCode(StringUtils.trimToNull(postalCode));
		billingAddress.setPhone(StringUtils.trimToNull(phone));
		billingAddress.setAltPhone(StringUtils.trimToNull(altPhone));
		
		account.setBillingAddress(billingAddress);

		account = accountRepo.save(account);
		
		return account.getBillingAddress();

	}

	/**
	 * Reset the password for the given email users.  The token verifies this was the person 
	 * originating the request.
	 */
	public void resetPassword(String email, String password, String token) throws AccountResetException {
		Account account = byEmail(email);
		if (account == null) {
			throw new AccountResetException("No account exist for " + email);
		}
		
		//check token matches
		if (account.getResetToken().equals(token)){
			
			//ensure token hasn't expired
			long expires = Long.valueOf(StringUtils.substring(account.getResetToken(), -13));
			if (System.currentTimeMillis() < expires) {
				updateAccountPassword(account, password);
				accountRepo.save(account);
				return;
			}
			
		}

		throw new AccountResetException("Unable to reset password");
	}

	public void changePassword(String email, String oldPassword, String newPassword) {
		verifyAccount(email, oldPassword);

		Account account = accountRepo.findByEmail(email);
		updateAccountPassword(account, newPassword);
		accountRepo.save(account);
	}


	/**
	 * Changes the account type of a user.
	 */
	public void changeAccountType(Long accountId, String type) {
		AccountType accountType = accountTypeRepo.findByName(type);
		Validate.notNull(accountType, "Invalid account type " + type);

		Account account = byId(accountId);
		Assert.notNull(account, NoAccountExistsException.class, "Account does not exists");

		account.setAccountType(accountType);

		accountRepo.save(account);
	}

	/**
	 * Suspends and account, no activity can occur with this account 
	 */
	public void suspendAccount(Long accountId, boolean status){

		Account account = byId(accountId);
		Validate.notNull(accountId, "Account does not exists");

		account.setSuspended(status);

		accountRepo.save(account);
	}

	public String generateResetPasswordToken(String email) {
		long timestamp = System.currentTimeMillis() + 24 * 60 * 60 * 1000;

		Account account = byEmail(email);

		byte[] seed = SecureRandom.getSeed(20);
		String token = DigestUtils.sha1Hex(seed.toString());
		token += timestamp;

		account.setResetToken(token);
		
		accountRepo.save(account);
		
		return token.toString();
	}


	@SuppressWarnings("serial")
	public class AccountResetException extends EcommerceException {
		
		public AccountResetException(String msg) {
			super(403, msg);
		}
		
	}
}
