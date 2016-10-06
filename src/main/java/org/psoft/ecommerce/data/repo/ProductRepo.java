package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, Long>{

	@SuppressWarnings("serial")
	public static class NoProductExists extends EcommerceException {

		public NoProductExists(Long id) {
			super(404, "No product exists for " + id);
		}
		
	}

	@SuppressWarnings("serial")
	public static class ProductAlreadyExists extends EcommerceException {

		public ProductAlreadyExists(Long id) {
			super(407, "Product already exists for " + id);
		}
		
	}

}
