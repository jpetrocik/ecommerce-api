package org.psoft.ecommerce.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.rest.view.CategoryView;
import org.psoft.ecommerce.rest.view.ItemView;
import org.psoft.ecommerce.rest.view.ProductView;
import org.psoft.ecommerce.service.CategoryService;
import org.psoft.ecommerce.service.ItemService;
import org.psoft.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog")
public class CatalogRestController {

	@Autowired
	CategoryService catalogService;

	@Autowired
	ProductService productService;

	@Autowired
	ItemService itemService;
	
	/**
	 * Returns the category, the optional depth parameter returns additional sub
	 * categories
	 * 
	 * @param categoryId
	 *            of the category
	 * @param depth
	 *            the number of nested sub categories to return
	 */
	@RequestMapping(path="/{categoryId}", method = {RequestMethod.GET})
	public CategoryView categoryById(@PathVariable Long categoryId,
			@RequestParam(required = false, defaultValue = "0") Integer depth) {
		Category category = catalogService.byId(categoryId);

		return CategoryView.create(category, depth, false);
	}

	@RequestMapping(path="/{categoryId}/product/", method = {RequestMethod.GET})
	public List<ProductView> productsByCategory(@PathVariable Long categoryId,
			@RequestParam(defaultValue="false") Boolean includeSubCategories) {
		
		List<Product> results = productService.byCategoryId(categoryId, false, includeSubCategories);
		return results.stream().map(p -> ProductView.create(p)).collect(Collectors.toList());
	}

	@RequestMapping(path="/product/{productId}", method = {RequestMethod.GET})
	public ProductView productById(@PathVariable Long productId) {
		Product product  = productService.byId(productId);
		return ProductView.create(product);
	}

	@RequestMapping(path="/product/{productId}/item/", method = {RequestMethod.GET})
	public List<ItemView> itemsByProduct(Long productId){
		return itemService.byProductId(productId).stream().map(i -> ItemView.create(i)).collect(Collectors.toList());
	}
	
	@RequestMapping(path="/item/{itemId}", method = {RequestMethod.GET})
	public List<ItemView> itemById(@PathVariable Long itemId) {
		List<Item> results = itemService.byProductId(itemId);
		return results.stream().map(i -> ItemView.create(i)).collect(Collectors.toList());
	}
	
}
