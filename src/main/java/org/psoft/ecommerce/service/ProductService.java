package org.psoft.ecommerce.service;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.data.model.AttributeName;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.data.repo.AttributeNameRepo;
import org.psoft.ecommerce.data.repo.AttributeNameRepo.NoAttributeNameExists;
import org.psoft.ecommerce.data.repo.CategoryRepo;
import org.psoft.ecommerce.data.repo.CategoryRepo.NoCategoryExists;
import org.psoft.ecommerce.data.repo.ItemRepo;
import org.psoft.ecommerce.data.repo.ItemRepo.NoItemExists;
import org.psoft.ecommerce.data.repo.ProductRepo;
import org.psoft.ecommerce.data.repo.ProductRepo.NoProductExists;
import org.psoft.ecommerce.data.repo.ProductRepo.ProductAlreadyExists;
import org.psoft.ecommerce.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Products represent the highest level of the items you sell.  For example a particular t-shirt would be a product. 
 * The different sizes and color would be the items.  
 */
@Service
public class ProductService extends AbstractService<Product> {
	private static Log log = LogFactory.getLog(ProductService.class);

	@Autowired
	CategoryRepo categoryRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ItemRepo itemRepo;
	
	@Autowired
	AttributeNameRepo attributeNameRepo;

	public Product saveProduct(Long id, Long categoryId, Boolean active, String name, String longDescr, String shortDescr, String keywords, 
			String productCode) {

		Product product;
		if (id != null) {
 			product = productRepo.findOne(id);
 			Assert.notNull(product, NoProductExists.class, id);
 		} else {
			product = new Product();
 		}

		Category category = null;
		if (categoryId != null) {
			category = findCategory(categoryId);
		}
		
		if (productCode != null)
			product.setProductCode(productCode);
		if (active != null)
			product.setActive(active);
		if (name != null)
			product.setName(
					Assert.value(name, "Name is required for a product"));
		if (longDescr != null)
			product.setLongDescr(longDescr);
		if (keywords != null)
			product.setKeywords(keywords);
		if (shortDescr != null)
			product.setShortDescr(shortDescr);

		product = productRepo.save(product);

		if (category != null) {
			category.addProduct(product);
			categoryRepo.save(category);
		}
		
		return product;
	}

	/**
	 * Returns the item by id
	 */
	public Product byId(Long id) {
		return productRepo.findOne(id);
	}

	/**
	 * Returns products for this category
	 * @param categoryId Id of the category
	 * @param includeInactive Whether only active product should be included
	 * @param includeAll Whether product from subCategories should be include
	 * @return matching product
	 */
	public List<Product> byCategoryId(Long categoryId, boolean inactive, boolean includeSubCategories) {
		Category category = findCategory(categoryId);

		if (includeSubCategories)
			return category.getAllProducts(inactive);
		else if (inactive)
			return category.getProducts();
		else
			return category.getActiveProducts();
	}

	/**
	 * Performs a full text index search on title, longDescr, keywords
	 */
	public List<Product> search(String search) {
		throw new NotImplementedException("New to implement product search");
//		log.info("Peforming search for " + search);
//
//		Validate.notEmpty(StringUtils.trimToNull(search));
//
//		Session session = currentSession();
//
//		Query query = session
//				.createSQLQuery("select p.* from product p where MATCH (code,title,longDescr,keywords) AGAINST (?);").addEntity(
//						Product.class);
//		query.setString(0, search);
//		List<Product> results = query.list();
//
//		log.info("Found " + results.size() + " results");
//		return results;
	}

	/**
	 * Moves an item from one product to another
	 */
	@Deprecated
	public void moveItem(Long oldProductId, Long newProductId, Long itemId) {

		Product oldProduct = findProduct(oldProductId);
		Product newProduct = findProduct(newProductId);

		Item item = findItem(itemId);

		oldProduct.removeItem(item);
		newProduct.addItem(item);

		productRepo.save(oldProduct);
		productRepo.save(newProduct);
	}

	/**
	 * Adds an item to a product
	 */
	public void addItem(Long productId, Long itemId) {

		Product product = findProduct(productId);
		Item item = findItem(itemId);

		boolean result = product.addItem(item);
		Validate.isTrue(result, "Unable to add item");

		productRepo.save(product);
	}

	public void addAttributeType(Long productId, String name) {
		Validate.notEmpty(StringUtils.trimToNull(name), "Attribute name required");

		AttributeName attributeName = new AttributeName();
		attributeName.setClassType(String.class.getName());
		attributeName.setName(name);

		Product product = findProduct(productId);
		product.addAttributeName(attributeName);

		productRepo.save(product);
	}

	public void removeAttributeType(Long productId, Long attributeNameId) {
		AttributeName attributeName = attributeNameRepo.findOne(attributeNameId);
		Assert.notNull(attributeName, NoAttributeNameExists.class, attributeNameId);
		
		Product product = findProduct(productId);
		product.removeAttributeName(attributeName);

		productRepo.save(product);
	}
	
	private Category findCategory(Long categoryId) {
		Category category = categoryRepo.findOne(categoryId);
		Assert.notNull(category, NoCategoryExists.class, categoryId);
		return category;
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
