package org.psoft.ecommerce.service.client.data;

import java.util.List;

import org.psoft.ecommerce.data.model.AttributeName;

import lombok.Data;

@Data
public class ProductClientView {

	Long id;

	boolean active;

	String name;

	String shortDescr;

	String longDescr;

	String keywords;

	String productCode;

	String productClass;
	
	List<AttributeName> attributes;

	List<CategoryPathClientView> paths;

	public static class AttributeNameClientView {
		
		Long id;

		String name;

		String classType;
	}
}
