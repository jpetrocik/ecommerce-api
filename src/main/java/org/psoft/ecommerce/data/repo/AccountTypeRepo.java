package org.psoft.ecommerce.data.repo;

import org.psoft.ecommerce.data.model.AccountType;
import org.springframework.data.repository.CrudRepository;

public interface AccountTypeRepo extends CrudRepository<AccountType, Long>{

	AccountType findByName(String name);

}
