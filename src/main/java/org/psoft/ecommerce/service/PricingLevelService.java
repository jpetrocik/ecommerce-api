package org.psoft.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.psoft.ecommerce.data.model.PricingLevel;
import org.psoft.ecommerce.data.repo.PricingLevelRepo;
import org.psoft.ecommerce.data.repo.PricingLevelRepo.NoPricingLevelExists;
import org.psoft.ecommerce.data.repo.PricingLevelRepo.PricingLevelAlreadyExists;
import org.psoft.ecommerce.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricingLevelService  extends AbstractService<PricingLevel> {

	@Autowired
	PricingLevelRepo pricingLevelRepo;
	
	public PricingLevel savePricingLevel(Long id, String name) {
		PricingLevel pricingLevel = pricingLevelRepo.findOne(id);
		if (id == null) {
			Assert.notNull(pricingLevel, PricingLevelAlreadyExists.class, id);
			pricingLevel = new PricingLevel();
		}
		Assert.notNull(pricingLevel, NoPricingLevelExists.class, id);

		pricingLevel.setName(Assert.value(name, "Prcing level name must not be blank"));
		
		return pricingLevelRepo.save(pricingLevel);
	}

	public List<PricingLevel> allPricingLevels() {
		List<PricingLevel> pricingLevels = new ArrayList<>();
		
		for (PricingLevel p : pricingLevelRepo.findAll()) {
			pricingLevels.add(p);
			
		}
		return pricingLevels;
	}


}
