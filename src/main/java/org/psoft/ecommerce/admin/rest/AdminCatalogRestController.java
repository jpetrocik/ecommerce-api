package org.psoft.ecommerce.admin.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.psoft.ecommerce.data.model.Category;
import org.psoft.ecommerce.data.model.Item;
import org.psoft.ecommerce.data.model.Product;
import org.psoft.ecommerce.rest.view.CategoryView;
import org.psoft.ecommerce.rest.view.ItemView;
import org.psoft.ecommerce.rest.view.PricingView;
import org.psoft.ecommerce.rest.view.ProductView;
import org.psoft.ecommerce.service.CategoryService;
import org.psoft.ecommerce.service.ItemService;
import org.psoft.ecommerce.service.PricingLevelService;
import org.psoft.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/catalog")
@CrossOrigin(origins = "*")
public class AdminCatalogRestController {

	@Autowired
	CategoryService catalogService;
	
	@Autowired
	ProductService productService;

	@Autowired
	ItemService itemService;
	
	@Autowired
	PricingLevelService pricingLevelService;
	
	@RequestMapping("/addCategory")
	public CategoryView addCategory(@RequestParam Long parentId, @RequestParam String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) Integer sequence,
			@RequestParam(required = false) Boolean active) {

		Category category = catalogService.saveCategory(null, parentId , active, name, longDescr, shortDescr, keywords,
				sequence);
		return CategoryView.create(category, 0, false);
	}

	@RequestMapping("/saveCategory")
	public CategoryView saveCategory(@RequestParam Long categoryId, @RequestParam(required = false) String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) Integer sequence,
			@RequestParam(required = false) Boolean active) {

		Category category = catalogService.saveCategory(categoryId, null , active, name, longDescr, shortDescr, keywords,
				sequence);
		return CategoryView.create(category, 0, false);
	}

	@RequestMapping("/deleteCategory")
	public void deleteCategory(@RequestParam Long categoryId) {
		catalogService.deleteCategory(categoryId);
	}

	@RequestMapping("/categoryById")
	public CategoryView categoryById(@RequestParam Long categoryId,
			@RequestParam(required = false, defaultValue = "0") Integer depth,
			@RequestParam(defaultValue="false") Boolean includeInactive) {
		Category category = catalogService.byId(categoryId);

		return CategoryView.create(category, depth, includeInactive);
	}

	@RequestMapping("/addToCategory")
	public void addSubcategory(@RequestParam Long parentId, @RequestParam Long categoryId,
			@RequestParam(required = false) Integer index) {
		catalogService.addSubcategory(parentId, categoryId, index);
	}

	@RequestMapping("/removeFromCategory")
	public void removeSubcategory(@RequestParam Long parentId, @RequestParam Long categoryId) {
		catalogService.removeSubcategory(parentId, categoryId);
	}

	@RequestMapping("/addProductToCategory")
	public void addProductToCategory(@RequestParam Long categoryId, @RequestParam Long productId) {
		catalogService.addProduct(categoryId, productId);
	}

	@RequestMapping("/removeProductFromCategory")
	public void removeProductFromCategory(@RequestParam Long categoryId, @RequestParam Long productId) {
		catalogService.removeProduct(categoryId, productId);
	}
	
	@RequestMapping("/productsByCategory")
	public List<ProductView> productsByCategory(@RequestParam Long categoryId,
			@RequestParam(defaultValue="false") Boolean includeSubCategories,
			@RequestParam(defaultValue="false") Boolean includeInactive) {
		
		List<Product> results = productService.byCategoryId(categoryId, includeInactive, includeSubCategories);
		return results.stream().map(p -> ProductView.create(p)).collect(Collectors.toList());
	}

	@RequestMapping("/productById")
	public ProductView productById(Long productId) {
		Product product  = productService.byId(productId);
		return ProductView.create(product);
	}

	@RequestMapping("/addProductAttribute")
	public void addProductAttribute(Long productId, String name) {
		productService.addAttributeType(productId, name);
	}

	@RequestMapping("/removeProductAttribute")
	public void removeProductAttribute(Long productId, Long attributeNameId) {
		productService.removeAttributeType(productId, attributeNameId);
	}

	@RequestMapping("/addProductItem")
	public void addProductItem(Long productId, Long itemId) {
		productService.addItem(productId, itemId);
	}

	@RequestMapping("/addProduct")
	public ProductView addProduct(@RequestParam Long categoryId, @RequestParam String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) String  productCode,
			@RequestParam(required = false) Boolean active) {

		Product product = productService.saveProduct(null, categoryId, active, name, longDescr, shortDescr, keywords, productCode);
		return ProductView.create(product);
	}

	@RequestMapping("/saveProduct")
	public ProductView saveProduct(@RequestParam Long productId, @RequestParam(required = false) String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) String productCode,
			@RequestParam(required = false) Boolean active) {

		Product product = productService.saveProduct(productId, null, active, name, longDescr, shortDescr, keywords, productCode);
		return ProductView.create(product);
	}

	@RequestMapping("/itemById")
	public List<ItemView> itemById(Long itemId) {
		List<Item> results = itemService.byProductId(itemId);
		return results.stream().map(i -> ItemView.create(i)).collect(Collectors.toList());
	}
	
	@RequestMapping("/addItemAttribute")
	public void addItemAttribute(Long itemId, Long attributeId, String value){
		itemService.addAttribute(itemId, attributeId, value);
	}
	
	@RequestMapping("/removeItemAttribute")
	public void removeItemAttribute(Long itemId, Long attributeValueId){
		itemService.removeAttribute(itemId, attributeValueId);
	}

	@RequestMapping("/addPricing")
	public void addPrice(Long itemId, Long attributeId, String value){
		itemService.addAttribute(itemId, attributeId, value);
	}

	@RequestMapping("/currentPricing")
	public PricingView currentPricing(Long item, String accountType){
		return PricingView.create(itemService.retireveCurrentPricing(item, accountType));
	}
	
	@RequestMapping("/itemByPartNum")
	public ItemView itemByPartNum(String partNum){
		return ItemView.create(itemService.byPartNumber(partNum));
	}
	
	@RequestMapping("/itemByProduct")
	public List<ItemView> itemsByProduct(Long productId){
		return itemService.byProductId(productId).stream().map(i -> ItemView.create(i)).collect(Collectors.toList());
	}
	
	@RequestMapping("/saveItem")
	public ItemView saveItem(Long itemId, Boolean active, String name, String longDescr, String shortDescr, String keywords, String partNum, String upc){
		Item item = itemService.saveItem(itemId, null, active, name, longDescr, shortDescr, keywords, partNum, upc);
		return ItemView.create(item);
	}
	
	@RequestMapping("/addItem")
	public ItemView addItem(Boolean active, Long productId, String name, String longDescr, String shortDescr, String keywords, String partNum, String upc){
		Item item = itemService.saveItem(null, productId, active, name, longDescr, shortDescr, keywords, partNum, upc);
		return ItemView.create(item);
	}
	
	public void createPricingLevel(String name) {
		savePricingLevel(null, name);
	}
	
	public void savePricingLevel(Long pricingLevelId, String name) {
		pricingLevelService.savePricingLevel(pricingLevelId, name);
	}
}
