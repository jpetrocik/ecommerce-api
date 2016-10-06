package org.psoft.ecommerce.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.psoft.ecommerce.data.model.Setting;

public class ApplicationSettings {

	SessionFactory sessionFactory;

	public String getString(String key, String defaultValue) {

		Session session = sessionFactory.getCurrentSession();

		Setting settings = (Setting) session.get(Setting.class, key);

		String value = defaultValue;

		if (settings != null && StringUtils.isNotBlank(settings.getValue()))
			value = settings.getValue();

		Validate.notNull(value,"No setting for " + key);
		
		return value;
	}

	public Double getDouble(String key, Double defaultValue) {

		Session session = sessionFactory.getCurrentSession();

		Setting settings = (Setting) session.get(Setting.class, key);

		Double value = defaultValue;
		if (settings != null && StringUtils.isNotBlank(settings.getValue())) {
			if (NumberUtils.isNumber(settings.getValue()))
				value = new Double(settings.getValue());
		}

		Validate.notNull(value,"No setting for " + key);

		return value;
	}

	public void save(String key, String value) {
		Session session = sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.AUTO);

		Setting setting = (Setting) session.get(Setting.class, key);
		if (setting == null) {
			setting = new Setting();
			setting.setValue(value);
			setting.setName(key);
			session.save(setting);
		} else {
			setting.setValue(value);
			session.update(setting);
		}

		session.flush();

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
