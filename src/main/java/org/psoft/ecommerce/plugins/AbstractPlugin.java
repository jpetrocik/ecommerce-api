package org.psoft.ecommerce.plugins;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.util.ApplicationSettings;

public abstract class AbstractPlugin implements Plugin {

	private ApplicationSettings applicationSettings;

	private SessionFactory sessionFactory;

	public String getAdminTemplate() {
		String className = this.getClass().getSimpleName();
		String packageName = "/" + this.getClass().getPackage().getName().replace('.', '/');

		return packageName + "/html/" + className + ".html";
	}

	public ApplicationSettings getApplicationSettings() {
		return applicationSettings;
	}

	public void setApplicationSettings(ApplicationSettings applicationSettings) {
		this.applicationSettings = applicationSettings;
	}

	public Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void saveSetting(String key, String value) {
		getApplicationSettings().save(key, value);
	}

	public void processFiles(FileUpload file1, FileUpload file2, FileUpload file3, FileUpload file4){
	}
	
	protected boolean isWholesaler(AccountType accountType){
		if (accountType == null)
			return false;
		
		return accountType.getName().contains("Wholesaler");
	}

	
}
