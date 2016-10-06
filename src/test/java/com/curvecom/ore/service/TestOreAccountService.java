package com.curvecom.ore.service;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.psoft.builders.MapBuilder;
import org.psoft.ecommerce.service.EmailService;
import org.psoft.ecommerce.util.ApplicationSettings;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.orm.hibernate.HibernateUnitils;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestOreAccountService extends UnitilsJUnit4 {

	@Mock
	EmailService mockEmailService;

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	ApplicationSettings applicationSettings;

	OreAccountService oreAccountService;

	@Before
	public void setUp() {
		applicationSettings = new ApplicationSettings();
		applicationSettings.setSessionFactory(sessionFactory);

		oreAccountService = new OreAccountService();
		oreAccountService.setEmailService(mockEmailService);
		oreAccountService.setSessionFactory(HibernateUnitils.getSessionFactory());
		oreAccountService.setApplicationSettings(applicationSettings);
	}

	@Test
	public void testCreateAccountOptIn() throws Exception {
		Map<String, String> data = new MapBuilder<String, String>().put("email", username).getMap();
		mockEmailService.sendPlainTextEmail("john@petrocik.net", "/opt_in.vm", data);

		EasyMockUnitils.replay();

		creatAccount(true, "Consumer");

		EasyMockUnitils.verify();

	}

	@Test
	public void testCreateAccountOptOut() throws Exception {

		EasyMockUnitils.replay();

		creatAccount(false, "Consumer");

		EasyMockUnitils.verify();

	}

	@Test
	public void testCreateAccountNonConsumer() throws Exception {
		Map<String, String> data2 = new MapBuilder<String, String>().put("email", username).put("billingName", billingName).put(
				"accountType", "Distributor").getMap();
		mockEmailService.sendPlainTextEmail(null, "/trade_access_request.vm", data2);

		EasyMockUnitils.replay();

		creatAccount(false, "Distributor");

		EasyMockUnitils.verify();

	}

	String username = "jdoe@somewhere.net";

	String password = "secure";

	String billingName = "Joe Doe";

	String billingCompany = null;

	String billingAddress1 = "555 Somewhere St.";

	String billingAddress2 = null;

	String billingCity = "Pleasenton";

	String billingState = "CA";

	String billingPostalCode = "555555";

	String billingPhone = "555-555-1234";

	String billingAltPhone = null;

	String shippingName = "Joe Doe";

	String shippingCompany = null;

	String shippingAddress1 = "555 Somewhere St.";

	String shippingAddress2 = null;

	String shippingCity = "Pleasenton";

	String shippingState = "CA";

	String shippingPostalCode = "555555";

	String shippingPhone = "555-555-1234";

	String shippingAltPhone = null;

	private void creatAccount(boolean optIn, String accountType) {

		oreAccountService.createAccount(username, password, billingName, billingCompany, billingAddress1, billingAddress2,
				billingCity, billingState, billingPostalCode, billingPhone, billingAltPhone, shippingName, shippingCompany,
				shippingAddress1, shippingAddress2, shippingCity, shippingState, shippingPostalCode, shippingPhone,
				shippingAltPhone, accountType, optIn);

	}

}
