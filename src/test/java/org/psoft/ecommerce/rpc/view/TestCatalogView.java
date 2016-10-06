package org.psoft.ecommerce.rpc.view;

import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.rest.view.CategoryPathView;
import org.psoft.ecommerce.rest.view.CategoryView;

public class TestCatalogView extends TestCase {
	private static Log log = LogFactory.getLog(TestCatalogView.class);

	public void testBuildPath() {
		Category root = createCategory("root", 0L);
		Category sub1 = createCategory("level1-A", 1L);
		Category sub2 = createCategory("level1-B", 2L);
		Category sub3 = createCategory("level1-C", 3L);
		Category sub4 = createCategory("level2-A", 4L);
		Category sub5 = createCategory("level3-A", 5L);

		root.addSubcategory(sub1);
		sub1.addSubcategory(sub4);
		sub4.addSubcategory(sub5);

		root.addSubcategory(sub2);
		sub2.addSubcategory(sub4);

		root.addSubcategory(sub3);
		sub3.addSubcategory(sub5);

		CategoryView view = new CategoryView(sub5);
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
