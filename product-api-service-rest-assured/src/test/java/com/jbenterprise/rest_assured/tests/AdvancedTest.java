package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jbenterprise.rest_assured.entity.ProductResponse;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class AdvancedTest {
	

	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8084";
    }
	
	@Test
    @DisplayName("GET products verify greater than zero - /api/v1/product/")
    public void getProductsVerifyUsingResponse() {
		System.out.println("START - Running AdvancedSuite getProductsVerifyUsingResponse - " + Thread.currentThread().getId());
    	 Response response = given()
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("/api/v1/product/")//Uri
    	.then()
    		.extract()
    		.response();
    	 
         Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
         Assertions.assertTrue(response.jsonPath().getBoolean("status"));
         assertThat(response.jsonPath().getList("products").size(), is(greaterThan(0)));
         System.out.println("END - Running AdvancedSuite getProductsVerifyUsingResponse - " + Thread.currentThread().getId());
    }
	
	@Test
    @DisplayName("GET products verify greater than zero using extract as - /api/v1/product/")
    public void getProductsVerifyUsinExtract() {
		System.out.println("START - Running AdvancedSuite getProductsVerifyUsinExtract - " + Thread.currentThread().getId());
    	 ProductResponse response = given()
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("/api/v1/product/")//Uri
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.extract()
    		.as(ProductResponse.class);
    	 
         Assertions.assertTrue(response.getStatus());
         assertThat(response.getProducts().size(), is(greaterThan(0)));
         //assertThat(response.getProducts(), hasSize(4));
         System.out.println("END - Running AdvancedSuite getProductsVerifyUsinExtract - " + Thread.currentThread().getId());
    }
}
