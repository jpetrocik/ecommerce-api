package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.AttributeValue;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.springframework.data.repository.CrudRepository;

public interface AttributeValueRepo  extends CrudRepository<AttributeValue, Long> {

	@SuppressWarnings("serial")
	public static class NoAttributeValueExists extends EcommerceException {

		public NoAttributeValueExists(Long id) {
			super(405, "No attribute value exists for " + id);
		}
		
	}

}
