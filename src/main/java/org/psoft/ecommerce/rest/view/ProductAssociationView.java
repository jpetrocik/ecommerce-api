package org.psoft.ecommerce.rest.view;

import org.psoft.ecommerce.data.model.ProductAssociation;

public class ProductAssociationView {

	ProductAssociation productAssociation;

	public ProductAssociationView(ProductAssociation productAssociation) {
		this.productAssociation = productAssociation;
	}

	public Long getId() {
		return productAssociation.getProduct().getId();
	}

	public String getTitle() {
		return productAssociation.getProduct().getTitle();
	}

	public String getType() {
		return productAssociation.getType();
	}

	public String getCode() {
		return productAssociation.getProduct().getProductCode();
	}

	public Boolean getIsActive() {
		return productAssociation.getProduct().getIsActive();
	}

}
