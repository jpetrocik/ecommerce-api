package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepo extends CrudRepository<Item, Long>{

	public Item findByPartNum(String partNum);

	@SuppressWarnings("serial")
	public static class NoItemExists extends EcommerceException {

		public NoItemExists(Long id) {
			super(405, "No item exists for " + id);
		}
		
	}

	@SuppressWarnings("serial")
	public static class ItemAlreadyExists extends EcommerceException {

		public ItemAlreadyExists(Long id) {
			super(408, "Item already exists for " + id);
		}
		
	}

}
