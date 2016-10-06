package org.psoft.ecommerce.rest.view;

import org.psoft.ecommerce.data.model.AttributeValue;

public class AttributeValueView {

	AttributeValue attributeValue;

	public AttributeValueView(AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getName() {
		return attributeValue.getAttributeName().getName();
	}

	public Long getId() {
		return attributeValue.getId();
	}

	public String getValue() {
		return attributeValue.getValue();
	}

}
