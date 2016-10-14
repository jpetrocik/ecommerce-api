package org.psoft.ecommerce.admin.rest;

import java.sql.Date;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(path="/{parentId}", method = {RequestMethod.POST})
	public CategoryView addCategory(@PathVariable Long parentId, @RequestParam String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) Integer sequence,
			@RequestParam(required = false) Boolean active) {

		Category category = catalogService.saveCategory(null, parentId , active, name, longDescr, shortDescr, keywords,
				sequence);
		return CategoryView.create(category, 0, false);
	}

	@RequestMapping(path="/{categoryId}", method = {RequestMethod.GET})
	public CategoryView categoryById(@PathVariable Long categoryId,
			@RequestParam(required = false, defaultValue = "0") Integer depth,
			@RequestParam(defaultValue="false") Boolean includeInactive) {
		Category category = catalogService.byId(categoryId);
		
		return CategoryView.create(category, depth, includeInactive);
	}

	@RequestMapping(path="/{categoryId}", method = {RequestMethod.PUT})
	public CategoryView saveCategory(@PathVariable Long categoryId, @RequestParam(required = false) String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) Integer sequence,
			@RequestParam(required = false) Boolean active) {

		Category category = catalogService.saveCategory(categoryId, null , active, name, longDescr, shortDescr, keywords,
				sequence);
		return CategoryView.create(category, 0, false);
	}

	@RequestMapping(path="/{categoryId}", method = {RequestMethod.DELETE})
	public void deleteCategory(@PathVariable Long categoryId) {
		catalogService.deleteCategory(categoryId);
	}

	@RequestMapping(path="/{parentId}/{categoryId}", method = {RequestMethod.PUT})
	public void addSubcategory(@PathVariable Long parentId, @PathVariable Long categoryId,
			@RequestParam(required = false) Integer index) {
		catalogService.addSubcategory(parentId, categoryId, index);
	}

	@RequestMapping(path="/{parentId}/{categoryId}", method = {RequestMethod.DELETE})
	public void removeSubcategory(@PathVariable Long parentId, @PathVariable Long categoryId) {
		catalogService.removeSubcategory(parentId, categoryId);
	}

	@RequestMapping(path="/{categoryId}/product/", method = {RequestMethod.GET})
	public List<ProductView> productsByCategory(@PathVariable Long categoryId,
			@RequestParam(defaultValue="false") Boolean includeSubCategories,
			@RequestParam(defaultValue="false") Boolean includeInactive) {
		
		List<Product> results = productService.byCategoryId(categoryId, includeInactive, includeSubCategories);
		return results.stream().map(p -> ProductView.create(p)).collect(Collectors.toList());
	}

	@RequestMapping(path="/{categoryId}/product/{productId}", method = {RequestMethod.PUT})
	public void addProductToCategory(@PathVariable Long categoryId, @PathVariable Long productId) {
		catalogService.addProduct(categoryId, productId);
	}

	@RequestMapping(path="/{categoryId}/product", method = {RequestMethod.POST})
	public ProductView addProduct(@PathVariable Long categoryId, @RequestParam String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) String  productCode,
			@RequestParam(required = false) Boolean active) {

		Product product = productService.saveProduct(null, categoryId, active, name, longDescr, shortDescr, keywords, productCode);
		return ProductView.create(product);
	}

	@RequestMapping(path="/{categoryId}/product/{productId}", method = {RequestMethod.DELETE})
	public void removeProductFromCategory(@PathVariable Long categoryId, @PathVariable Long productId) {
		catalogService.removeProduct(categoryId, productId);
	}

	@RequestMapping(path="/product/{productId}", method = {RequestMethod.GET})
	public ProductView productById(@PathVariable Long productId) {
		Product product  = productService.byId(productId);
		return ProductView.create(product);
	}

	@RequestMapping(path="/product/{productId}", method = {RequestMethod.PUT})
	public ProductView saveProduct(@PathVariable Long productId, @RequestParam(required = false) String name,
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) String productCode,
			@RequestParam(required = false) Boolean active) {

		Product product = productService.saveProduct(productId, null, active, name, longDescr, shortDescr, keywords, productCode);
		return ProductView.create(product);
	}

	@RequestMapping(path="/product/{productId}/attribute", method = {RequestMethod.PUT})
	public void addProductAttribute(@PathVariable Long productId, @RequestParam String name) {
		productService.addAttributeType(productId, name);
	}

	@RequestMapping(path="/product/{productId}/attribute/{attributeNameId}", method = {RequestMethod.DELETE})
	public void removeProductAttribute(@PathVariable Long productId, @PathVariable Long attributeNameId) {
		productService.removeAttributeType(productId, attributeNameId);
	}

	@RequestMapping(path="/product/{productId}/item/", method = {RequestMethod.POST})
	public ItemView addItem(@PathVariable Long productId, @RequestParam String name, @RequestParam(required = false) String longDescr, 
			@RequestParam(required = false) String shortDescr, @RequestParam(required = false) String keywords, @RequestParam String partNum, 
			@RequestParam(required = false) String upc, @RequestParam(defaultValue = "true") Boolean active){
		Item item = itemService.saveItem(null, productId, active, name, longDescr, shortDescr, keywords, partNum, upc);
		return ItemView.create(item);
	}
	
	@RequestMapping(path="/product/{productId}/item/", method = {RequestMethod.GET})
	public List<ItemView> itemsByProduct(Long productId){
		return itemService.byProductId(productId).stream().map(i -> ItemView.create(i)).collect(Collectors.toList());
	}
	
	@RequestMapping(path="/product/{productId}/item/{itemId}", method = {RequestMethod.PUT})
	public void addProductItem(@PathVariable Long productId, @PathVariable Long itemId) {
		productService.addItem(productId, itemId);
	}

	@RequestMapping(path="/item/{itemId}", method = {RequestMethod.GET})
	public List<ItemView> itemById(@PathVariable Long itemId) {
		List<Item> results = itemService.byProductId(itemId);
		return results.stream().map(i -> ItemView.create(i)).collect(Collectors.toList());
	}
	
	@RequestMapping(path="/item/{itemId}/attribute/{attributeId}", method = {RequestMethod.PUT})
	public void addItemAttribute(@PathVariable Long itemId, @PathVariable Long attributeId, @RequestParam String value){
		itemService.addAttribute(itemId, attributeId, value);
	}
	
	@RequestMapping(path="/item/{itemId}/attribute/{attributeValueId}", method = {RequestMethod.DELETE})
	public void removeItemAttribute(@PathVariable Long itemId, @PathVariable Long attributeValueId){
		itemService.removeAttribute(itemId, attributeValueId);
	}

	@RequestMapping(path="/item/{itemId}/pricing", method = {RequestMethod.GET})
	public PricingView currentPricing(@PathVariable Long item, @RequestParam String accountType){
		return PricingView.create(itemService.retireveCurrentPricing(item, accountType));
	}
	
	@RequestMapping(path="/item/{itemId}/pricing", method = {RequestMethod.PUT})
	public void addPrice(@PathVariable Long itemId, @RequestParam String level, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate, 
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate, 
			@RequestParam Double price, @RequestParam(defaultValue = "1") Integer minimum,  @RequestParam(defaultValue = "1") Integer multiplier){
		itemService.addPrice(itemId, level, startDate, endDate, price, minimum, multiplier);
	}

	@RequestMapping(path = "/item", method = {RequestMethod.GET})
	public ItemView itemByPartNum(@RequestParam String partNum){
		return ItemView.create(itemService.byPartNumber(partNum));
	}
	
	
	@RequestMapping(path="/item/{itemId}", method = {RequestMethod.PUT})
	public ItemView saveItem(@PathVariable Long itemId, @RequestParam(defaultValue = "true") Boolean active, @RequestParam String name, 
			@RequestParam(required = false) String longDescr, @RequestParam(required = false) String shortDescr, @RequestParam(required = false) String keywords,
			@RequestParam String partNum, @RequestParam(required = false)  String upc){
		Item item = itemService.saveItem(itemId, null, active, name, longDescr, shortDescr, keywords, partNum, upc);
		return ItemView.create(item);
	}
	
	@RequestMapping(path = "/pricingLevel", method = {RequestMethod.POST})
	public void addPricingLevel(@RequestParam String name) {
		pricingLevelService.savePricingLevel(null, name);
	}
}
