package org.psoft.ecommerce.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.rest.view.CategoryView;
import org.psoft.ecommerce.rest.view.ItemView;
import org.psoft.ecommerce.rest.view.ProductView;
import org.psoft.ecommerce.service.CategoryService;
import org.psoft.ecommerce.service.ItemService;
import org.psoft.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog")
public class CatalogRestController {

	@Autowired
	CategoryService catalogService;

	@Autowired
	ProductService productService;

	/**
	 * Returns the category, the optional depth parameter returns additional sub
	 * categories
	 * 
	 * @param id
	 *            of the category
	 * @param depth
	 *            the number of nested sub categories to return
	 */
	@RequestMapping("/category")
	public CategoryView category(@RequestParam Long id,
			@RequestParam(required = false, defaultValue = "0") Integer depth) {
		Category category = catalogService.byId(id);

		return CategoryView.create(category, depth);
	}

	@RequestMapping("/productsByCategory")
	public List<ProductView> productsByCategoryId(Long id) {
		List<ProductView> results = productService.byCategoryId(id).stream().map(p -> ProductView.create(p))
				.collect(Collectors.toList());

		return results;
	}

	@RequestMapping("/product")
	public ProductView product(Long id) {
		ProductView productView = ProductView.create(productService.byId(id));

		return productView;
	}

	@RequestMapping("/itemsByProduct")
	public List<ItemView> itemsByProductId(Long id) {
		// List<ItemView> results = itemService.retrieveByProductId(id).stream()
		// .map(i -> ItemView.create(i)).collect(Collectors.toList());
		//
		// return results;
		return null;
	}

	@RequestMapping("/item")
	public ItemView item(Long id) {
		// Item item = itemService.retrieveById(id);
		//
		// return ItemView.create(item);
		return null;
	}

}
