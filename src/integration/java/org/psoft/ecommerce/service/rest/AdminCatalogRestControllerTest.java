package org.psoft.ecommerce.service.rest;

import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.ECommerceApplication;
import org.psoft.ecommerce.rest.view.ProductView;
import org.psoft.ecommerce.service.client.data.CategoryClientView;
import org.psoft.ecommerce.service.client.data.ProductClientView;
import org.springframework.boot.SpringApplication;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

@DataSet
public class AdminCatalogRestControllerTest extends UnitilsJUnit4 {

	private static final String BASEURL = "http://localhost:8080";

	@Before
	public void setup(){
//		String[] args = new String[] {};
//		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Test
	public void testCatalogRegression() throws InterruptedException {
		
		//drivetrain
		CategoryClientView parentCategory = addCategory("DRIVETRAIN", 1L);
		
		//drivetrain/chainrings
		parentCategory = addCategory("Chainrings", parentCategory.getId());

		//drivetrain/chainrings/chainring bolts
		CategoryClientView category = addCategory("Chainring Bolts", parentCategory.getId());
		deactiveCategory(category.getId());

		//drivetrain/chainrings/chainrings
		category = addCategory("Chainring", parentCategory.getId());

		//add product
		ProductClientView product = addProduct("Race Face Single Narrow Wide Chainring", category.getId());
		
		//add product
		product = addProduct("Race Face Cinch Narrow Wide Chainring", category.getId());
		deactiveProduct(product.getId());
		
	}
	
	private ProductClientView addProduct(String name, Long categoryId) {
		Response response = RestAssured.given().queryParam("categoryId", categoryId)
		.queryParam("name", name).expect().statusCode(200).get(BASEURL + "/admin/catalog/addProduct");

		ProductClientView product = response.as(ProductClientView.class);
		
		response = RestAssured.given().queryParam("productId", product.getId())
		.queryParam("name", "Color").expect().statusCode(200).get(BASEURL + "/admin/catalog/addProductAttribute");

		return product;
	}

	private ProductClientView deactiveProduct(Long id) {
		Response response = RestAssured.given().queryParam("productId", id)
		.queryParam("active", "false").expect().statusCode(200).get(BASEURL + "/admin/catalog/saveProduct");

		return response.as(ProductClientView.class);
	}

	private CategoryClientView addCategory(String name, Long parentId) {
		Response response = RestAssured.given().queryParam("parentId", parentId)
		.queryParam("name", name).expect().statusCode(200).get(BASEURL + "/admin/catalog/addCategory");

		return response.as(CategoryClientView.class);
	}

	private CategoryClientView deactiveCategory(Long id) {
		Response response = RestAssured.given().queryParam("categoryId", id)
		.queryParam("active", "false").expect().statusCode(200).get(BASEURL + "/admin/catalog/saveCategory");

		return response.as(CategoryClientView.class);
	}

}
