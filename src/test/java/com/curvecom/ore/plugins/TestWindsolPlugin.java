package com.curvecom.ore.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Address;
import org.psoft.ecommerce.data.model.Order;
import org.psoft.ecommerce.data.model.Payment;
import org.psoft.ecommerce.plugins.Plugin.FileUpload;
import org.psoft.ecommerce.util.ApplicationSettings;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@HibernateSessionFactory("hibernate.cfg.xml")
public class TestWindsolPlugin extends UnitilsJUnit4 {

	WindsolPlugin windsolPlugin;
	
	@HibernateSessionFactory
	SessionFactory sessionFactory;

	@Mock
	ApplicationSettings applicationSettings;

	@Mock 
	Order order;
	
	@Mock
	Account account;
	
	@Mock
	Address address;
	
	@Mock
	Payment payment;

	@Before
	public void setUp(){
		windsolPlugin = new WindsolPlugin();
		windsolPlugin.setApplicationSettings(applicationSettings);
		windsolPlugin.setSessionFactory(sessionFactory);
	}
	
	@Test
	public void testSaveFiles() throws IOException{

		//setup test dir
		String testDir = String.valueOf(Math.random());
		File writeDir = new File("/tmp/"+testDir);
		FileUtils.forceMkdir(writeDir);
		
		EasyMock.expect(applicationSettings.getString("warehouse.inbound.dir", "/tmp/")).andReturn(writeDir.getAbsolutePath()+"/");
		EasyMockUnitils.replay();
		
		FileUpload ship = new FileUpload();
		InputStream inStream1 = TestWindsolPlugin.class.getResourceAsStream("/test_ship.csv");
		ship.setData(IOUtils.toByteArray(inStream1));

		FileUpload track = new FileUpload();
		InputStream inStream2 = TestWindsolPlugin.class.getResourceAsStream("/test_track.csv");
		track.setData(IOUtils.toByteArray(inStream2));
		
		windsolPlugin.saveFiles(ship, track);

		//check CRC32 ship file
		Collection<File> shipFiles = FileUtils.listFiles(writeDir, new String[] {"ship"}, false);
		Assert.assertEquals(2186535969L, FileUtils.checksumCRC32(shipFiles.iterator().next()));

		//check CRC32  of track file
		Collection<File> trackFiles = FileUtils.listFiles(writeDir, new String[] {"track"}, false);
		Assert.assertEquals(2489742693L, FileUtils.checksumCRC32(trackFiles.iterator().next()));

		//Clean up
		FileUtils.deleteDirectory(writeDir);
	}
	
//	@Test
	public void testSubmitOrders() throws Exception{

		List<Order> orders = new ArrayList<Order>();
		orders.add(order);
		
		String testDir = String.valueOf(Math.random());
		File writeDir = new File("/tmp/"+testDir);
		EasyMock.expect(applicationSettings.getString(WindsolPlugin.OUTBOUND_DIR_SETTING, null)).andReturn(writeDir.getAbsolutePath()+"/");

		Set<Payment> payments = new HashSet<Payment>();
		payments.add(payment);
		
		EasyMock.expect(order.getShippingAddress()).andReturn(address).anyTimes();
		EasyMock.expect(order.getBillingAddress()).andReturn(address).anyTimes();
		EasyMock.expect(order.getCreatedOn()).andReturn(new Date()).anyTimes();
		EasyMock.expect(order.getCreatedBy()).andReturn("john").anyTimes();
		EasyMock.expect(order.getOrderNum()).andReturn("ORDERNUM").anyTimes();
		EasyMock.expect(order.getDetails()).andReturn(new ArrayList()).anyTimes();
		EasyMock.expect(order.getAccount()).andReturn(account).anyTimes();
		EasyMock.expect(order.getPayments()).andReturn(payments).anyTimes();

		EasyMock.expect(payment.getType()).andReturn("Net30").anyTimes();
		
		EasyMock.expect(account.getId()).andReturn(1l).anyTimes();
		
		EasyMock.expect(address.getName()).andReturn("Name").anyTimes();
		EasyMock.expect(address.getCompany()).andReturn("Company").anyTimes();
		EasyMock.expect(address.getAddress1()).andReturn("Address1").anyTimes();
		EasyMock.expect(address.getAddress2()).andReturn("Address2").anyTimes();
		EasyMock.expect(address.getCity()).andReturn("City").anyTimes();
		EasyMock.expect(address.getPhone()).andReturn("Phone").anyTimes();
		EasyMock.expect(address.getPostalCode()).andReturn("Postal").anyTimes();
		EasyMock.expect(address.getState()).andReturn("CA").anyTimes();
		EasyMockUnitils.replay();

		windsolPlugin.submitOrders(orders);
		

	}
}
