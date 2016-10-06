package com.curvecom.ore.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.plugins.Plugin.FileUpload;
import org.psoft.ecommerce.util.ApplicationSettings;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

import com.curvecom.ore.data.OreInventory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestOrderInventroyPlugin  extends UnitilsJUnit4 {

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	OreInventoryPlugin plugin;

	@Before
	public void setup(){
		
		ApplicationSettings applicationSettings = new ApplicationSettings();
		applicationSettings.setSessionFactory(sessionFactory);
		
		plugin = new OreInventoryPlugin();
		plugin.setSessionFactory(sessionFactory);
		plugin.setApplicationSettings(applicationSettings);
	}
	
	@Test
	public void processFiles() throws IOException{
		InputStream input1 = TestOrderInventroyPlugin.class.getResourceAsStream("/stock.csv");
		FileUpload stock = new FileUpload();
		stock.setData(IOUtils.toByteArray(input1));


		InputStream input2 = TestOrderInventroyPlugin.class.getResourceAsStream("/inventory.csv");
		FileUpload inventory = new FileUpload();
		inventory.setData(IOUtils.toByteArray(input2));
		
		Map<String,OreInventory> stockStatus = new HashMap<String,OreInventory>();

		plugin.processInventoryFile(inventory, stockStatus);
		plugin.processStockFile(stock, stockStatus);

		OreInventory a481 = stockStatus.get("A481");
		Assert.assertNotNull(a481);
		Assert.assertEquals("10/01/10", a481.getStock());
		Assert.assertEquals(NumberUtils.LONG_ZERO, a481.getAvailable());
	}

	@Test
	public void processStockFile() throws IOException{
		InputStream input = TestOrderInventroyPlugin.class.getResourceAsStream("/stock.csv");
		Map<String,OreInventory> inventory = new HashMap<String,OreInventory>();

		FileUpload fileUpload = new FileUpload();
		fileUpload.setData(IOUtils.toByteArray(input));
		
		
		plugin.processStockFile(fileUpload, inventory);
		
		OreInventory a768 = inventory.get("A768");
		Assert.assertNotNull(a768);
		Assert.assertEquals("10/25/10", a768.getStock());
		
		OreInventory a414 = inventory.get("A414");
		Assert.assertNotNull(a414);
		Assert.assertEquals("12/15/10", a414.getStock());
		
	}
}
