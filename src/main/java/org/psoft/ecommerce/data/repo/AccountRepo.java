package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.rest.view.EcommerceException;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long>{

	Account findByEmail(String email);

	@SuppressWarnings("serial")
	public class AccountExistsException extends EcommerceException {

		public AccountExistsException(String message) {
			super(401, message);
		}
	}

	@SuppressWarnings("serial")
	public class NoAccountExistsException extends EcommerceException {

		public NoAccountExistsException(String message) {
			super(402, message);
		}
	}

}
