package org.psoft.ecommerce.service;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.service.AccountService.AccountExistsException;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestAccountService extends UnitilsJUnit4 {

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	@Mock
	private EmailService mockEmailService;

	AccountService accountService;

	@Before
	public void setUp() {
		accountService = new AccountService();
		accountService.setEmailService(mockEmailService);
		accountService.setSessionFactory(sessionFactory);
	}

	@Test
	public void testRetrieveById() {

		Account account = accountService.byId(1L);

		Assert.assertNotNull(account);
	}

	@Test
	public void testVerifyAccount() throws Exception {
		// HibernateUnitils.flushDatabaseUpdates();

		// correct username and password
		Account account = accountService.verifyAccount("john@amce.com", "apple");

		Assert.assertNotNull(account);
		Assert.assertEquals("john@amce.com", account.getEmail());

		// incorrect username and password
		account = accountService.verifyAccount("nobody@amce.com", "apple");

		Assert.assertNull(account);

	}

	@Test
	public void testFindAccount() throws Exception {

		// valid account
		Account account = accountService.byUsername("john@amce.com");

		Assert.assertNotNull(account);
		Assert.assertEquals("john@amce.com", account.getEmail());

		// invalid account
		account = accountService.verifyAccount("nobody@amce.com", "apple");

		Assert.assertNull(account);

	}

	@Test
	public void testSearchAccount() throws Exception {
		List<Account> accounts = accountService.searchAccount("john do%", " ", null);

		// search by billing address name
		Assert.assertEquals(1, accounts.size());
		Assert.assertEquals("john@amce.com", accounts.get(0).getEmail());

		// search by email address
		accounts = accountService.searchAccount("", "john@%", null);

		Assert.assertEquals(1, accounts.size());
		Assert.assertEquals("john@amce.com", accounts.get(0).getEmail());

		// search by billing address phone
		accounts = accountService.searchAccount("", "", "555-555-1234");

		Assert.assertEquals(1, accounts.size());
		Assert.assertEquals("john@amce.com", accounts.get(0).getEmail());
	}

	@Test
	public void testCreateAccount() {

		Account newAccount = accountService.createAccount("jane@amce.com", "orange", "Jane Doe", "", "555 Somewhere Ln", "",
				"Someplace", "CA", "90111", "555-555-1234", "", "John Doe", "", "555 Somewhere Else", "", "Somewhere", "CA",
				"092222", "555-555-4321", null, null);

		Assert.assertEquals("jane@amce.com", newAccount.getEmail());
		Assert.assertEquals("orange", newAccount.getPassword());
		Assert.assertEquals("Retail", newAccount.getType().getName());

		Assert.assertEquals("Jane Doe", newAccount.getBillingAddress().getName());
		Assert.assertNull(newAccount.getBillingAddress().getCompany());
		Assert.assertEquals("555 Somewhere Ln", newAccount.getBillingAddress().getAddress1());
		Assert.assertNull(newAccount.getBillingAddress().getAddress2());
		Assert.assertEquals("Someplace", newAccount.getBillingAddress().getCity());
		Assert.assertEquals("CA", newAccount.getBillingAddress().getState());
		Assert.assertEquals("90111", newAccount.getBillingAddress().getPostalCode());
		Assert.assertEquals("555-555-1234", newAccount.getBillingAddress().getPhone());
		Assert.assertNull(newAccount.getBillingAddress().getAltPhone());

		Assert.assertEquals("John Doe", newAccount.getShippingAddress().getName());
		Assert.assertNull(newAccount.getShippingAddress().getCompany());
		Assert.assertEquals("555 Somewhere Else", newAccount.getShippingAddress().getAddress1());
		Assert.assertNull(newAccount.getShippingAddress().getAddress2());
		Assert.assertEquals("Somewhere", newAccount.getShippingAddress().getCity());
		Assert.assertEquals("CA", newAccount.getShippingAddress().getState());
		Assert.assertEquals("092222", newAccount.getShippingAddress().getPostalCode());
		Assert.assertEquals("555-555-4321", newAccount.getShippingAddress().getPhone());
		Assert.assertNull(newAccount.getShippingAddress().getAltPhone());
	}

	@Test
	public void testCreateDuplicateAccount() {
		try {
			accountService.createAccount("john@amce.com", "orange", "Jane Doe", "", "555 Somewhere Ln", "", "Someplace", "CA",
					"90111", "555-555-1234", "", "John Doe", "", "555 Somewhere Else", "", "Somewhere", "CA", "092222",
					"555-555-4321", null, "customer");
			throw new AssertionFailedError("Duplicate account created");
		} catch (AccountExistsException aee) {

		}
	}

	@Test
	public void testOrderHistory() throws Exception {

		List<Order> results = accountService.getOrderHistory(1l);

		Assert.assertEquals(1, results.size());

		Order order = results.get(0);
		Assert.assertEquals("100-1-1", order.getOrderNum());
	}

	@Test
	public void testGetOrder() {
		Order order = accountService.getOrder(1L, 1l);

		Assert.assertEquals("100-1-1", order.getOrderNum());
	}

	@Test
	public void testSendPasswordEmail() throws Exception {
		Account account = accountService.byUsername("john@amce.com");

		mockEmailService.sendPlainTextEmail("john@amce.com", "/forgot_password.vm", account);
		EasyMockUnitils.replay();

		accountService.resetPassword("john@amce.com");

		EasyMockUnitils.verify();
	}

	@Test
	@ExpectedDataSet
	public void testUpgradeAccount() {

		accountService.changeAccountType(1L, "Wholesale");

	}

}
