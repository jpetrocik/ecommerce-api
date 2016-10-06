package org.psoft.ecommerce.data;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.test.DomainUtils;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

@DataSet
@HibernateSessionFactory("hibernate.cfg.xml")
public class TestCategory extends UnitilsJUnit4 {

	@HibernateSessionFactory
	private SessionFactory sessionFactory;

	@Test
	public void testAllCategoriesIncludeInActive(){

		Session session = sessionFactory.getCurrentSession();

		Category cat1 = (Category)session.get(Category.class, 1L);
		List<Product> products = cat1.getAllProducts(true);

		Assert.assertEquals(3, products.size());
	}
	
	@Test
	public void testAllCategoriesInlcudeActiveOnly(){

		Session session = sessionFactory.getCurrentSession();

		Category cat1 = (Category)session.get(Category.class, 1L);
		List<Product> products = cat1.getAllProducts(false);

		Assert.assertEquals(1, products.size());
	}

	@ExpectedDataSet
	@Test
	public void testCreateCategory() throws Exception {

		Session session = sessionFactory.getCurrentSession();

		Category cat1 = (Category)session.get(Category.class, 1L);
		Category cat9 = DomainUtils.createCategory("category9");
		Category cat10 = DomainUtils.createCategory("category10");

		Product product6 = (Product)session.get(Product.class, 6L);
		Product product11 = DomainUtils.createProduct("product11");

		cat1.addSubcategory(cat9);
		cat9.addSubcategory(cat10);

		cat10.addProduct(product11);
		cat10.addProduct(product6);

		session.saveOrUpdate(cat1);
		session.flush();

	}

	@ExpectedDataSet
	@Test
	public void testRemoveSubCategoryWithProduct() throws Exception {

		Session session = (Session) sessionFactory.getCurrentSession();

		Category category = (Category) session.get(Category.class, 1L);
		Category subcategory = category.getSubcategories().get(0);
		category.removeSubcategory(subcategory);

		session.save(category);
		session.flush();

	}

	@ExpectedDataSet
	@Test
	public void testRemoveSubCategoryWithCategory() throws Exception {

		Session session = (Session) sessionFactory.getCurrentSession();

		Category category = (Category) session.get(Category.class, 1L);
		Category subcategory = category.getSubcategories().get(1);
		category.removeSubcategory(subcategory);

		session.save(category);
		session.flush();

	}

	@ExpectedDataSet
	@Test
	public void testCategoryDelete() throws Exception {

		Session session = (Session) sessionFactory.getCurrentSession();

		Category category = (Category) session.get(Category.class, 1L);

		session.delete(category);
		
		session.flush();

	}
	
	@ExpectedDataSet
	@Test
	public void testAddImage() throws Exception {

		Session session = (Session) sessionFactory.getCurrentSession();

		ImageData image1 = createImage("52_24_3_0.jpg", "Plum", 0);
		ImageData image2 = createImage("54_23_2_0.jpg", "Green", 0);
		ImageData image3 = createImage("55_23_1_0.jpg", "Black", 0);

		session.saveOrUpdate(image1);
		session.saveOrUpdate(image2);
		session.saveOrUpdate(image3);

		Category category = (Category) session.get(Category.class, 1L);
		category.addImage(image1);
		category.addImage(image2);
		category.addImage(image3);

		session.saveOrUpdate(category);
		session.flush();
	}

	private ImageData createImage(String filename, String alt, int sequence) throws Exception {
		InputStream stream = TestCategory.class.getResourceAsStream("/" + filename);
		byte[] data = IOUtils.toByteArray(stream);

		ImageData image = new ImageData();
		image.setRaw(data);
		image.setFilename(filename);
		image.setAlt(alt);
		image.setSequence(sequence);

		return image;
	}
	
}
