package org.psoft.ecommerce.service;

import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.poi.util.IOUtils;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestImageService extends UnitilsJUnit4 {

	@TestDataSource
	DataSource dataSource;

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	ImageService imageService;

	@Before
	public void setUp() {
		imageService = new ImageService();
		imageService.setSessionFactory(sessionFactory);
	}

	@Test
	@ExpectedDataSet
	public void testCreateNewImage() throws Exception {

		Long id = 1l;
		String alt = "Alternative Text";
		String description = "Description";
		String filename = "52_24_3_0.jpg";
		String mimeType = "image/jpg";
		String group = "product";

		InputStream inStream = TestImageService.class.getResourceAsStream("/52_24_3_0.jpg");
		byte[] raw = IOUtils.toByteArray(inStream);

		imageService.createNewImage(id, group, alt, description, filename, mimeType, raw);
	}

}
