package org.psoft.ecommerce.service.rest;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.psoft.ecommerce.service.client.data.CategoryClientView;
import org.psoft.ecommerce.service.client.data.ProductClientView;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

@DataSet(value = {"CleanCatalog.xml"})
public class LoadBikeCatalog extends UnitilsJUnit4 {

	private static final String BASEURL = "http://localhost:8080";

	@Test
	public void loadJensonUSA() throws IOException{
		List<String> data = IOUtils.readLines(LoadBikeCatalog.class.getResourceAsStream("/bike_catalog.txt"));
		
		Long rootCategory = 1L;

		boolean first = true;
		Long parent = rootCategory;
		for (String l : data) {
			
			if (StringUtils.isBlank(l)) {
				parent = rootCategory;
				first = true;
				continue;
			}
			
			if (first) {
				parent = addCategory(l, parent).getId();
				first = false;
			} else {
				addCategory(l, parent).getId();
			}
				
		}
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
