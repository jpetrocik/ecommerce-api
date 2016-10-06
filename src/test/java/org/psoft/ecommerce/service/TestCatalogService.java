package org.psoft.ecommerce.service;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Category;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestCatalogService extends UnitilsJUnit4 {

	@TestDataSource
	DataSource dataSource;

	@HibernateSessionFactory
	SessionFactory sessionFactory;

	CategoryService catalogService;

	@Before
	public void setUp() {
		catalogService = new CategoryService();
		catalogService.setSessionFactory(sessionFactory);
	}

	@Test
	public void testRetrieveById() throws Exception {
		Category category = catalogService.retrieveById(1L);
		Assert.assertEquals("category1", category.getTitle());
	}

	@Test
	@ExpectedDataSet
	public void testDeleteCategory() throws Exception {
		catalogService.deleteCategory(7L);
	}
	
	@Test
	public void testRetrieveAllCategories() throws Exception {
		List<Category> results = catalogService.retrieveAllCategories();
		Assert.assertEquals(7, results.size());
	}

}
