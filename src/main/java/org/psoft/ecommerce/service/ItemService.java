package org.psoft.ecommerce.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.psoft.ecommerce.data.model.AccountType;
import org.psoft.ecommerce.data.model.AttributeName;
import org.psoft.ecommerce.data.model.AttributeValue;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Pricing;
import org.psoft.ecommerce.data.model.PricingLevel;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.data.repo.AccountTypeRepo;
import org.psoft.ecommerce.data.repo.AttributeNameRepo;
import org.psoft.ecommerce.data.repo.AttributeNameRepo.NoAttributeNameExists;
import org.psoft.ecommerce.data.repo.AttributeValueRepo;
import org.psoft.ecommerce.data.repo.AttributeValueRepo.NoAttributeValueExists;
import org.psoft.ecommerce.data.repo.ItemRepo;
import org.psoft.ecommerce.data.repo.ItemRepo.ItemAlreadyExists;
import org.psoft.ecommerce.data.repo.ItemRepo.NoItemExists;
import org.psoft.ecommerce.data.repo.PricingLevelRepo;
import org.psoft.ecommerce.data.repo.PricingLevelRepo.NoPricingLevelExists;
import org.psoft.ecommerce.data.repo.PricingRepo;
import org.psoft.ecommerce.data.repo.ProductRepo;
import org.psoft.ecommerce.data.repo.ProductRepo.NoProductExists;
import org.psoft.ecommerce.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends AbstractService<Item> {
	private static Log log = LogFactory.getLog(ItemService.class);

	@Autowired
	ProductRepo productRepo;

	@Autowired
	ItemRepo itemRepo;

	@Autowired
	PricingRepo pricingRepo;

	@Autowired
	PricingLevelRepo pricingLevelRepo;

	@Autowired
	AccountTypeRepo accountTypeRepo;

	@Autowired
	AttributeNameRepo attributeNameRepo;

	@Autowired
	AttributeValueRepo attributeValueRepo;

	/**
	 * Creates or updates an item 
	 */
	public Item saveItem(Long id, Long productId, Boolean isActive, String name, String longDescr, String shortDescr, String keywords, String partNum, String upc) {
		Item item = null;
		if (id != null) {
			item = itemRepo.findOne(id);
			Assert.notNull(item, NoItemExists.class, id);
		} else {
			item = new Item();
		}

		Product product = null;
		if (productId != null) {
			product = findProduct(productId);
		}

		if (partNum != null)
			item.setPartNum(Assert.value(partNum, "Part number must not be empty"));
		if (upc != null)
			item.setUpc(StringUtils.trimToNull(upc));
		if (name != null)
			item.setName(Assert.value(name, "Title must not be empty"));
		if (longDescr != null)
			item.setLongDescr(StringUtils.trimToNull(longDescr));
		if (shortDescr != null)
			item.setShortDescr(StringUtils.trimToNull(shortDescr));
		if (keywords != null)
			item.setKeywords(StringUtils.trimToNull(keywords));
		if (isActive != null)
			item.setActive(isActive);

		item = itemRepo.save(item);

		if (product != null) {
			product.addItem(item);
			productRepo.save(product);
		}
		
		return item;

	}

	/**
	 * Returns the item by id
	 */
	public Item byId(Long id) {
		log.debug("Fetching item with id " + id);
		return itemRepo.findOne(id);
	}

	/**
	 * Returns all the items for a given product
	 */
	public List<Item> byProductId(Long productId) {
		log.debug("Fetching product with id " + productId);
		Product product = findProduct(productId);

		return product.getItems();
	}

	/**
	 * Returns all the items for a given product
	 */
	public Item byPartNumber(String partNum) {
		log.debug("Fetching item " + partNum);
		
		return itemRepo.findByPartNum(
				Assert.value(partNum, "Part number must not be empty"));
	}

	/**
	 * Performs a full text index search on title, longDescr, keywords
	 */
	@SuppressWarnings("unchecked")
	public List<Item> search(String search) {
		throw new NotImplementedException("Need to implement item search");
//
//		search = StringUtils.trimToNull(search);
//		Validate.notNull(search, "Empty search is not allowed");
//
//		Session session = currentSession();
//
//		log.info("Peforming search for " + search);
//
//		Query query = session
//				.createSQLQuery("select i.* from item i where MATCH (partNum,title,longDescr,keywords) AGAINST (?);").addEntity(
//						Item.class);
//		query.setString(0, search);
//		List<Item> results = query.list();
//
//		return results;
	}

	/**
	 * Extends expiration date the pricing
	 */
	public void updatePriceExpiration(Long priceId, Date endDate) {
		

		Pricing price = pricingRepo.findOne(priceId);
		Assert.notNull(price, "No pricing exists for " + priceId);

		price.setEndDate(
				Assert.notNull(endDate, "Pricing end date is required"));

		pricingRepo.save(price);
	}

	/**
	 * Adds a price for this item 
	 */
	public Pricing addPrice(Long itemId, String level, Date startDate, Date endDate, Double price, Integer minimum, Integer mulitpilier) {

		level = StringUtils.trimToNull(level);
		Assert.value(level, "Level must not be empty");
		Assert.notNull(startDate, "Start date must not be empty");
		Assert.notNull(price, "Price must not be empty");

		Item item = findItem(itemId);

		PricingLevel pricingLevel = pricingLevelRepo.findByName(level);
		Assert.notNull(pricingLevel, NoPricingLevelExists.class, level);

		Pricing pricing = new Pricing();
		pricing.setEndDate(endDate);
		pricing.setStartDate(startDate);
		pricing.setPrice(price);
		pricing.setPricingLevel(pricingLevel);
		pricing.setMinimum(minimum);
		pricing.setMultiplier(mulitpilier);

		item.addPricing(pricing);

		itemRepo.save(item);
		
		return pricing;
	}

	public Pricing retireveCurrentPricing(Long itemId, String forAccountType) {
		Item item = findItem(itemId);

		forAccountType = StringUtils.trimToNull(forAccountType);

		AccountType accountType = accountTypeRepo.findByName(forAccountType);

		return retireveCurrentPricing(item, accountType);
	}

	public Pricing retireveCurrentPricing(Item item, AccountType accountType) {
		return retireveCurrentPricing(item, accountType, true);
	}

	public Pricing retireveCurrentPricing(Item item, AccountType accountType, boolean useDefault) {
		Pricing currentPrice = null;

		if (accountType != null) {

			Date today = new Date();

			for (Pricing price : item.getActivePricing()) {
				Date endDate = price.getEndDate();
				Date startDate = price.getStartDate();
				PricingLevel level = price.getPricingLevel();

				// right pricing level
				if (accountType.getPricingLevels().contains(level)) {

					// first time set current price
					if (currentPrice == null) {
						currentPrice = price;
						continue;
					}

					// start date before today, and after current price
					if (startDate.before(today) && startDate.after(currentPrice.getStartDate())) {

						// end date null or after today
						if (endDate == null || endDate.after(today)) {
							currentPrice = price;
						}

					}
				}
			}
		}

		// account might be null if useDefault is true
		if (currentPrice == null && useDefault) {
			//TODO Implement isDefault select
			AccountType defaultAccountType = accountTypeRepo.findByName("default");
			currentPrice = retireveCurrentPricing(item, defaultAccountType, false);
		}

		return currentPrice;
	}

	public void addAttribute(Long itemId, Long attributeId, String value) {
		AttributeName attributeName = attributeNameRepo.findOne(attributeId);
		Assert.notNull(attributeName, NoAttributeNameExists.class, attributeId);

		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setValue(Assert.value(value, "Attribute value must not be empty"));
		attributeValue.setAttributeName(attributeName);

		Item item = findItem(itemId);

		item.addAttributeValue(attributeValue);

		itemRepo.save(item);
	}

	public void removeAttribute(Long itemId, Long attributeValueId) {

		AttributeValue attributeValue = attributeValueRepo.findOne(attributeValueId);
		Assert.notNull(attributeValue, NoAttributeValueExists.class, attributeValueId);

		Item item = findItem(itemId);
		item.removeAttributeValue(attributeValue);
		
		itemRepo.save(item);
	}
	
	private Product findProduct(Long productId) {
		Product product = productRepo.findOne(productId);
		Assert.notNull(product, NoProductExists.class, productId);
		return product;
	}

	private Item findItem(Long itemId) {
		Item item = itemRepo.findOne(itemId);
		Assert.notNull(item, NoItemExists.class, itemId);
		return item;
	}

}
