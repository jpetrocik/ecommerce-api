package org.psoft.ecommerce.service;

import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.data.repo.CategoryRepo;
import org.psoft.ecommerce.data.repo.ProductRepo;
import org.psoft.ecommerce.data.repo.CategoryRepo.CategoryAlreadyExists;
import org.psoft.ecommerce.data.repo.CategoryRepo.NoCategoryExists;
import org.psoft.ecommerce.data.repo.ProductRepo.NoProductExists;
import org.psoft.ecommerce.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends AbstractService<Category> {
	private static final Log log = LogFactory.getLog(CategoryService.class);

	@Autowired
	CategoryRepo categoryRepo;

	@Autowired
	ProductRepo productRepo;

	public Category saveCategory(Long id, Long parentId, Boolean isActive, String name, String longDescr,
			String shortDescr, String keywords, Integer sequence) {
		
		Category category;
		if (id != null) {
			category = categoryRepo.findOne(id);
			Assert.notNull(category, NoCategoryExists.class, id);
		} else {
			category = new Category();
		}

		Category parent = null;
		if (parentId != null) {
			 parent = findCategory(parentId);
		}
		
		if (isActive != null)
			category.setActive(isActive);
		if (name != null)
			category.setName(Assert.value(name, "Name is required"));
		if (longDescr != null)
			category.setLongDescr(longDescr);
		if (shortDescr != null)
			category.setShortDescr(shortDescr);
		if (keywords != null)
			category.setKeywords(keywords);

		category = categoryRepo.save(category);

		if (parent != null) {
			if (sequence != null)
				parent.addSubcategory(category, sequence);
			else {
				parent.addSubcategory(category);
			}
			parent = categoryRepo.save(parent);
		}
			
		return category;
	}

	/**
	 * Returns category by primary key
	 */
	public Category byId(Long id) {
		Category category = findCategory(id);
		return category;
	}

	public void addProduct(Long categoryId, Long productId) {
		Category category =  findCategory(categoryId);
		
		Product product = findProduct(productId);

		boolean results = category.addProduct(product, 0);
		Validate.isTrue(results, "Unable to add product");

		categoryRepo.save(category);
	}

	public void removeProduct(Long categoryId, Long productId) {
		Category category = categoryRepo.findOne(categoryId);
		Product product = productRepo.findOne(productId);

		category.removeProduct(product);

		categoryRepo.save(category);
	}

	public void addSubcategory(Long parentId, Long childId, Integer index) {
		Category parent =  findCategory(parentId);
		Category child =  findCategory(childId);

		// remove first if exists
		parent.removeSubcategory(child);

		boolean result = false;
		if (index != null) {
			if (index > parent.getSubcategories().size()) {
				index = parent.getSubcategories().size();
			}
			result = parent.addSubcategory(child, index);
		} else
			result = parent.addSubcategory(child);
		Validate.isTrue(result, "Unable to add subcategory");

		categoryRepo.save(parent);
	}

	public void removeSubcategory(Long parentId, Long childId) {
		Category parent =  findCategory(parentId);
		Category child =  findCategory(childId);

		parent.removeSubcategory(child);
		Validate.notEmpty(child.getParents(), "Can not remove last parent");

		categoryRepo.save(parent);
	}

	@Deprecated
	public void addParent(Long childId, Long parentId) {
		Category parent = findCategory(parentId);
		Category child =  findCategory(childId);

		boolean results = parent.addSubcategory(child);
		Validate.isTrue(results, "Can not add category twice");

		categoryRepo.save(parent);
	}

	public void deleteCategory(Long categoryId) {
		Category category =  findCategory(categoryId);

		Validate.isTrue(category.getSubcategories().isEmpty(), "Remove all subcategories before deleting");

		Set<Category> allParents = category.getParents();
		for (Category parent : allParents) {
			parent.removeSubcategory(category);
		}

		categoryRepo.save(allParents);
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
}
