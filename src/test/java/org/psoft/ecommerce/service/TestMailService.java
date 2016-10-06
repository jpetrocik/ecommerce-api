package org.psoft.ecommerce.service;

import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.util.ApplicationSettings;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestMailService extends UnitilsJUnit4 {

	EmailService mailService;
	
	ApplicationSettings applicationSettings;

	@HibernateSessionFactory
	SessionFactory sessionFactory;


	@Before
	public void setUp() throws Exception {

		mailService = new EmailService();

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("mail.petrocik.net");

		applicationSettings = new ApplicationSettings();
		applicationSettings.setSessionFactory(sessionFactory);
		
		mailService.setApplicationSettings(applicationSettings);
		mailService.setSessionFactory(sessionFactory);
		mailService.setSender(mailSender);
	}
	
	@Test
	public void testSendMail() throws Exception {

		Map<String,String> context = new HashMap<String,String>();
		context.put("firstName", "John");
		context.put("lastName", "Petrocik");
		mailService.sendPlainTextEmail("john@petrocik.net", "/test.vm", context);
	}

	@Test
	public void testNoRelaySendMail() throws Exception {

		Map<String,String> context = new HashMap<String,String>();
		context.put("firstName", "John");
		context.put("lastName", "Petrocik");

		mailService.noRelaySendPlainTextEmail("john@petrocik.net", "/test.vm", context);

		try {
			mailService.noRelaySendPlainTextEmail("john@petrocik.com", "/test.vm", context);
			assertFailure("Allowed relay for email");
		} catch (Exception e) {
		}
	}

	private void assertFailure(String msg) {
		throw new AssertionFailedError(msg);
	}
}
