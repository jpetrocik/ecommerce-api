package org.psoft.ecommerce.rest.view;

import org.psoft.ecommerce.data.model.AttributeName;

public class AttributeNameView {

	AttributeName attributeName;

	public AttributeNameView(AttributeName attributeName) {
		this.attributeName = attributeName;
	}

	public Long getId() {
		return attributeName.getId();
	}

	public String getName() {
		return attributeName.getName();
	}

}
