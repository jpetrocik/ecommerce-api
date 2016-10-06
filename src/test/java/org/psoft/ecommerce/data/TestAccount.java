package org.psoft.ecommerce.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.test.DomainUtils;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@HibernateSessionFactory("hibernate.cfg.xml")
public class TestAccount extends UnitilsJUnit4 {

	@HibernateSessionFactory
	private SessionFactory sessionFactory;

	@Test
	@ExpectedDataSet
	public void createAccount() throws Exception {
		Session session = sessionFactory.getCurrentSession();

		Address billingAddress = DomainUtils.createAddress("John Doe", "111 Nowhere St.", "Long Beach", "CA", "90815");

		Address shippingAddress = DomainUtils.createAddress("John Doe", "111 Nowhere St.", "Long Beach", "CA", "90815");

		AccountType accountType = new AccountType();
		accountType.setName("Consumer");

		Account account = DomainUtils.createAccount("john@email.com", "password");
		account.setType(accountType);
		account.setBillingAddress(billingAddress);
		account.setShippingAddress(shippingAddress);

		session.saveOrUpdate(account);
	}

}
