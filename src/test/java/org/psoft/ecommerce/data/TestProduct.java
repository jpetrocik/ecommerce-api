package org.psoft.ecommerce.data;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Product;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;


@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestProduct extends UnitilsJUnit4 {

	@TestDataSource
	DataSource dataSource;

	@HibernateSessionFactory
	private SessionFactory sessionFactory;

	@ExpectedDataSet
	@Test
	public void testRemoveItem() throws Exception {

		Session session = (Session) sessionFactory.getCurrentSession();

		Product product = (Product) session.get(Product.class, 1L);
		List<Item> items = product.getItems();
		Item itemToRemove = items.get(0);
		
		product.removeItem(itemToRemove);

		session.save(product);
		session.flush();

	}
}
