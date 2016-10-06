package org.psoft.ecommerce.rest.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.psoft.ecommerce.data.model.Category;

public class CategoryPathView {

	final Stack<Category> path = new Stack<Category>();

	public CategoryPathView(Category category) {
		this.path.push(category);
	}

	public void push(Category category) {
		path.push(category);
	}

	public List<PathElevemtClientView> getPath() {
		List<PathElevemtClientView> breadCrumb = new ArrayList<>();

		for (Category c : path.subList(1, path.size())) {
			breadCrumb.add(new PathElevemtClientView(c.getId(), c.getName()));
		}

		return breadCrumb;
	}

	/**
	 * Build a list of category path.
	 * 
	 * @param category
	 * @param include
	 *            : include self in path
	 * @return
	 */
	public static List<CategoryPathView> buildPaths(final Category category, boolean include) {
		List<CategoryPathView> paths = new ArrayList<CategoryPathView>();
		Set<Category> parents = category.getParents();

		// stop recursion, at top to tree
		if (parents.isEmpty()) {
			CategoryPathView categoryPathView = new CategoryPathView(category);
			paths.add(categoryPathView);
			return paths;
		}

		// recursively call on parts
		for (Category parent : parents) {
			paths.addAll(buildPaths(parent, true));
		}

		if (include) {
			// push self onto path
			CollectionUtils.transform(paths, new Transformer() {

				public Object transform(Object input) {
					CategoryPathView cpv = (CategoryPathView) input;
					cpv.push(category);

					return cpv;
				}
			});
		}

		return paths;
	}
	
	public static class PathElevemtClientView {
		Long id;

		String name;
		
		public PathElevemtClientView(Long id, String name){
			this.id = id;
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
