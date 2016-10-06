package com.curvecom.ore.plugins;

import java.util.List;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.data.model.ShippingRate;
import org.psoft.ecommerce.util.ApplicationSettings;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestOreShippingPlugin   extends UnitilsJUnit4 {

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	OreShippingPlugin plugin;

	@Before
	public void setup(){
		
		ApplicationSettings applicationSettings = new ApplicationSettings();
		applicationSettings.setSessionFactory(sessionFactory);
		
		plugin = new OreShippingPlugin();
		plugin.setSessionFactory(sessionFactory);
		plugin.setApplicationSettings(applicationSettings);
	}
	
	@Test
	public void testLookupShippingOptions(){
		List<ShippingRate> results = plugin.lookupShippingOptions(null, null, null, 39.00);
		
		Assert.assertEquals(1, results.size());
		
		ShippingRate shipppingRate = results.get(0);
		
		Assert.assertEquals(10.5,shipppingRate.getRate());
	}

}
