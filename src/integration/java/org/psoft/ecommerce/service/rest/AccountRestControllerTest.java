package org.psoft.ecommerce.service.rest;

import org.junit.Before;
import org.junit.Test;
import org.psoft.ecommerce.ECommerceApplication;
import org.psoft.ecommerce.service.client.data.AccountClientView;
import org.springframework.boot.SpringApplication;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

@DataSet
public class AccountRestControllerTest extends UnitilsJUnit4 {

	private static final String BASEURL = "http://localhost:8080";

	@Before
	public void setup(){
		String[] args = new String[] {};
//		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Test
	public void testCreateAccountRegression() throws InterruptedException {
		
		//create duplicate account
		RestAssured.given().queryParam("email", "joe@gmail.com")
		.queryParam("password", "password")
		.queryParam("accountType", "default").expect().statusCode(500).get(BASEURL + "/account/create");

		//create new account
		Response response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("password", "password")
				.queryParam("accountType", "default").expect().statusCode(200).get(BASEURL + "/account/create");
		AccountClientView accountView = response.as(AccountClientView.class);

		//set shipping address
		response = RestAssured.given().queryParam("accountId", accountView.getId())
				.queryParam("name", "John Petrocik")
				.queryParam("address1", "555 Avenue 64")
				.queryParam("city", "Pasadena")
				.queryParam("state", "CA")
				.queryParam("postalCode", "91001")
				.queryParam("phone", "626-555-1711").expect().statusCode(200).get(BASEURL + "/account/defaultShippingAddress");

		//set billing address
		response = RestAssured.given().queryParam("accountId", accountView.getId())
				.queryParam("name", "John Petrocik")
				.queryParam("address1", "555 Avenue 64")
				.queryParam("city", "Pasadena")
				.queryParam("state", "CA")
				.queryParam("postalCode", "91001")
				.queryParam("phone", "626-555-1711").expect().statusCode(200).get(BASEURL + "/account/defaultBillingAddress");


		//attempt failed login
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("password", "changeme")
				.expect().statusCode(500).get(BASEURL + "/account/login");

		//attempt login
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("password", "password")
				.expect().statusCode(200).get(BASEURL + "/account/login");
		String session = response.asString();
		System.out.println("Session token " + session);

		//forgot password request
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.expect().statusCode(200).get(BASEURL + "/account/forgotPassword");
		String token = response.asString();
		System.out.println("Password reset token " + token);
		
		//failed request reset password
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("password", "newpassword")
				.queryParam("token", token+"1").expect().statusCode(500).get(BASEURL + "/account/resetPassword");

		//request reset password
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("password", "newpassword")
				.queryParam("token", token).expect().statusCode(200).get(BASEURL + "/account/resetPassword");

		//login with new password
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("password", "newpassword")
				.expect().statusCode(200).get(BASEURL + "/account/login");
		session = response.asString();
		System.out.println("Session token " + session);

		//failed request change password
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("oldPassword", "changeme")
				.queryParam("newPassword", "otherpassword")
				.queryParam("token", token).expect().statusCode(500).get(BASEURL + "/account/changePassword");

		//failed request change password
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("oldPassword", "newpassword")
				.queryParam("newPassword", "otherpassword")
				.queryParam("token", token).expect().statusCode(200).get(BASEURL + "/account/changePassword");

		//login with new password
		response = RestAssured.given().queryParam("email", "john@petrocik.net")
				.queryParam("password", "otherpassword")
				.expect().statusCode(200).get(BASEURL + "/account/login");
		session = response.asString();
		System.out.println("Session token " + session);
}
}
