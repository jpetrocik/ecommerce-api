package org.psoft.ecommerce.service;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Product;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestProductService extends UnitilsJUnit4 {

	@TestDataSource
	DataSource dataSource;

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	ProductService productService;

	@Before
	public void setUp() {
		productService = new ProductService();
		productService.setSessionFactory(sessionFactory);
	}

	@Test
	public void testRetrieveById() throws Exception {

		List<Product> products = productService.retrieveByCategoryId(1L);
		Assert.assertEquals(2, products.size());
	}

	@Test
	public void testSearch() throws Exception {

		List<Product> products = productService.search("redish");
		Assert.assertEquals(1, products.size());
	}

}
