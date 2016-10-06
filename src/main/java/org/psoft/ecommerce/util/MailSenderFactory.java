package org.psoft.ecommerce.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class MailSenderFactory implements Factory{
	private static Log log = LogFactory.getLog(MailSenderFactory.class);
	
	ApplicationSettings applicationSettings;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Object newInstance(){
            String host = applicationSettings.getString("email.smtp", "localhost");

            log.info("Creating mailSender for host  " + host);

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(host);
            
            return mailSender;
    }

	public void setApplicationSettings(ApplicationSettings applicationSettings) {
		this.applicationSettings = applicationSettings;
	}

}
