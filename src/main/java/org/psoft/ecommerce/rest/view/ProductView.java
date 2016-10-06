package org.psoft.ecommerce.rest.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.psoft.ecommerce.data.model.AttributeName;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.data.model.ProductAssociation;

public class ProductView {

	private Product product;

	List<CategoryPathView> paths = new ArrayList<CategoryPathView>();

//	List<ProductAssociationView> associations = new ArrayList<ProductAssociationView>();

	List<AttributeNameView> attributes = new ArrayList<AttributeNameView>();

	public static ProductView create(Product product){
		return new ProductView(product);
	}
	
	private ProductView(Product product) {
		this.product = product;

		Set<Category> parents = product.getCategories();
		for (Category category : parents) {
			paths.addAll(CategoryPathView.buildPaths(category, true));
		}

//		Set<ProductAssociation> allAssociations = product.getAssociatedProducts();
//		for (ProductAssociation association : allAssociations) {
//			associations.add(new ProductAssociationView(association));
//		}

		Set<AttributeName> allAttributes = product.getAttributes();
		for (AttributeName attribute : allAttributes) {
			attributes.add(new AttributeNameView(attribute));
		}
	}

	public Long getId() {
		return product.getId();
	}

	public boolean isActive() {
		return product.isActive();
	}

	public String getKeywords() {
		return product.getKeywords();
	}

	public String getProductCode() {
		return product.getProductCode();
	}

	public String getLongDescr() {
		return product.getLongDescr();
	}

	public String getShortDescr() {
		return product.getShortDescr();
	}

	public String getName() {
		return product.getName();
	}

	public List<CategoryPathView> getPaths() {
		return paths;
	}

//	public List<ProductAssociationView> getAssociations() {
//		return associations;
//	}

	public List<AttributeNameView> getAttributes() {
		return attributes;
	}

}
