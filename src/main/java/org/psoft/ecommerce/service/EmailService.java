package org.psoft.ecommerce.service;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.psoft.ecommerce.data.model.EmailTemplate;
import org.psoft.ecommerce.util.ApplicationSettings;
import org.psoft.ecommerce.util.CollectionUtils;
import org.psoft.ecommerce.util.DateServerUtil;
import org.psoft.ecommerce.util.VelocityUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * <p>
 * Service class that handles sending emails.
 * </p>
 * <p>
 * Required properties:
 * <ul>
 * <li>email.domain: Used to check email relay
 * <li>email.default.sender: Used for the from address
 * </ul>
 * 
 * @author jpetrocik
 * 
 */
public class EmailService {
	private static Log log = LogFactory.getLog(EmailService.class);

	private ApplicationSettings applicationSettings;

	private SessionFactory sessionFactory;

	private JavaMailSender sender;

	public void noRelaySendPlainTextEmail(String to, String template, Object context) throws Exception {
		String domain = applicationSettings.getString("email.domain", null);

		if (StringUtils.isNotBlank(domain) && !to.endsWith(domain)) {
			throw new Exception(to + " not in relay host list");
		}

		sendPlainTextEmail(to, template, context);
	}

	public void sendPlainTextEmail(String to, String template, Object context) throws Exception {

		EmailTemplate emailTemplate = retrieveEmailTemplate(template);

		if (emailTemplate == null) {
			log.warn("No email template configure for " + template);
			return;
		}

		String defaultSender = applicationSettings.getString("email.sender", null);
		String rcpt = (StringUtils.isNotBlank(emailTemplate.getMailFrom()) ? emailTemplate.getMailFrom() : defaultSender);
		if (StringUtils.isBlank(rcpt)) {
			log.warn("Unable to send email because there is no email.default.sender configured.");
			return;
		}

		// create a message
		MimeMessage msg = sender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(msg);
		helper.setTo(to);
		helper.setFrom(rcpt);
		helper.setSubject(emailTemplate.getSubject());
		helper.setSentDate(DateServerUtil.currentDate().getTime());

		// generate body
		String body = VelocityUtil.parse(emailTemplate.getPlainTemplate(), context);
		helper.setText(body);

		// send the message
		sender.send(msg);
	}

	private EmailTemplate retrieveEmailTemplate(String name) {
		Session session = sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(EmailTemplate.class).add(Restrictions.eq("name", name));
		List<EmailTemplate> results = c.list();

		return (EmailTemplate) CollectionUtils.firstElement(results);

	}

	public JavaMailSender getSender() {
		return sender;
	}

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public ApplicationSettings getApplicationSettings() {
		return applicationSettings;
	}

	public void setApplicationSettings(ApplicationSettings applicationSettings) {
		this.applicationSettings = applicationSettings;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
