package org.psoft.ecommerce.rest.view;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.psoft.ecommerce.data.model.Category;

public class CategoryView {

	Category category;

	List<CategoryPathView> paths;

	List<CategoryView> subCategories;
	
	public static CategoryView create(Category category, int depth, boolean includeInactive) {
		return new CategoryView(category, depth, includeInactive);
	}
	
	private CategoryView(Category category, Integer depth, boolean includeInactive) {
		this.category = category;

		paths = CategoryPathView.buildPaths(category, false);
		
		if (depth > 0 ) {
			buildSubCategories(category, depth, includeInactive);
		}
	}

	protected void buildSubCategories(Category category, int depth, boolean includeInactive){
		final int nextDepth = depth-1;
		
		if (includeInactive) {
			subCategories = category.getSubcategories()
					.stream().map(c -> CategoryView.create(c, nextDepth, includeInactive)).collect(Collectors.toList());
		} else {
			subCategories = category.getActiveSubcategories()
				.stream().map(c -> CategoryView.create(c, nextDepth, includeInactive)).collect(Collectors.toList());
		}
	}
	
	public Long getId() {
		return category.getId();
	}

	public String[] getKeywords() {
		if (StringUtils.isNotBlank(category.getKeywords()))
			return category.getKeywords().split(",");
		return null;
	}

	public String getLongDescr() {
		return category.getLongDescr();
	}

	public String getShortDescr() {
		return category.getShortDescr();
	}

	public boolean hasSubcategories() {
		return !category.getSubcategories().isEmpty();
	}

	public String getName() {
		return category.getName();
	}

	public boolean isActive() {
		return category.isActive();
	}

	public List<CategoryPathView> getPaths() {
		return paths;
	}

	public List<CategoryView> getSubCategories() {
		return subCategories;
	}

}
