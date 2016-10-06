package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<Category, Long>{

	
	@SuppressWarnings("serial")
	public static class NoCategoryExists extends EcommerceException {

		public NoCategoryExists(Long id) {
			super(403, "No category exists for " + id);
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class CategoryAlreadyExists extends EcommerceException {

		public CategoryAlreadyExists(Long id) {
			super(406, "Category already exists for " + id);
		}
		
	}

}
