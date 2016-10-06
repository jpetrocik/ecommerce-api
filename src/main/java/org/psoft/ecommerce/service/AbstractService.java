package org.psoft.ecommerce.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractService<T> implements ApplicationContextAware {
	private static Log log = LogFactory.getLog(AbstractService.class);
	
	private ApplicationContext applicationContext;

//	@SuppressWarnings("unchecked")
//	protected Plugin lookupPlugin(Class clazz) {
//		Map<String, Plugin> plugins = applicationContext.getBeansOfType(clazz);
//
//		Validate.notEmpty(plugins, "No plugin configure for " + clazz.getName());
//
//		// return the first one
//		Set<Map.Entry<String, Plugin>> entries = plugins.entrySet();
//		Plugin requestPlugin = entries.iterator().next().getValue();
//
//		return requestPlugin;
//	}
//
//	@SuppressWarnings("unchecked")
//	protected Collection<Plugin> pluginsByClass(Class clazz) {
//		Map<String, Plugin> plugins = applicationContext.getBeansOfType(clazz);
//
//		Validate.notEmpty(plugins, "No plugin configure for " + clazz.getName());
//
//		return plugins.values();
//	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
