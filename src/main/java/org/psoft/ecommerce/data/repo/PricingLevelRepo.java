package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.PricingLevel;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.springframework.data.repository.CrudRepository;

public interface PricingLevelRepo extends CrudRepository<PricingLevel, Long>{

	public PricingLevel findByName(String name);

	@SuppressWarnings("serial")
	public static class NoPricingLevelExists extends EcommerceException {

		public NoPricingLevelExists(Long id) {
			super(405, "No pricing level exists for " + id);
		}

		public NoPricingLevelExists(String name) {
			super(405, name + "pricing level does not exists");
		}

	}

	@SuppressWarnings("serial")
	public static class PricingLevelAlreadyExists extends EcommerceException {

		public PricingLevelAlreadyExists(Long id) {
			super(408, "Pricing level already exists for " + id);
		}
		
	}

}
