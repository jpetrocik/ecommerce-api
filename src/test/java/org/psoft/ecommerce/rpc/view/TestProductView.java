package org.psoft.ecommerce.rpc.view;

import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.rest.view.CategoryPathView;
import org.psoft.ecommerce.rest.view.ProductView;
import org.psoft.ecommerce.test.DomainUtils;

public class TestProductView extends TestCase {
	private static Log log = LogFactory.getLog(TestProductView.class);

	public void testBuildPath() throws Exception {
		Category root = createCategory("root", 0L);
		Category l1a = createCategory("level1-A", 1L);
		Category l1b = createCategory("level1-B", 2L);
		Category l1c = createCategory("level1-C", 3L);
		Category l2a = createCategory("level2-A", 4L);
		Category l3a = createCategory("level3-A", 5L);

		Product product = (Product) DomainUtils.createProduct("product1");

		root.addSubcategory(l1a);
		l1a.addSubcategory(l2a);
		l2a.addSubcategory(l3a);
		l3a.addProduct(product);

		root.addSubcategory(l1b);
		l1b.addSubcategory(l2a);
		l2a.addProduct(product);

		root.addSubcategory(l1c);
		l1c.addSubcategory(l3a);
		// l3a.addProduct(product);

		ProductView view = new ProductView(product);
		List<CategoryPathView> paths = view.getPaths();

		log.info(paths);
	}

	protected Category createCategory(String title, Long id) {
		Category root = new Category();
		root.setId(id);
		root.setTitle(title);

		return root;
	}

}
