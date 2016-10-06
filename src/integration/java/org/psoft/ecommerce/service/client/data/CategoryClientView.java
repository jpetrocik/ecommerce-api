package org.psoft.ecommerce.service.client.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class CategoryClientView {

	Long id;

	boolean active;

	String name;

	String shortDescr;

	String longDescr;

	String keywords;

	Set<CategoryClientView> parents = new HashSet<CategoryClientView>();
	
	List<CategoryClientView> subCategories = new ArrayList<CategoryClientView>();
	
	List<CategoryPathClientView> paths;
	
}
