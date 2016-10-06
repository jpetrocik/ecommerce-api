package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.AttributeName;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.springframework.data.repository.CrudRepository;

public interface AttributeNameRepo  extends CrudRepository<AttributeName, Long> {

	@SuppressWarnings("serial")
	public static class NoAttributeNameExists extends EcommerceException {

		public NoAttributeNameExists(Long id) {
			super(405, "No attribute exists for " + id);
		}
		
	}

}
