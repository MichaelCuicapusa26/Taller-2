package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.utils.Utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
public class ParameterTest {
	private static String sku="";
	private static RequestSpecification requestSpec;
	
	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8084";
        
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("User-Agent", Utils.USER_AGENT);
        builder.setContentType(ContentType.JSON);
        builder.addHeader("Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk");
        requestSpec = builder.build();
        
    	
    }   
	
	@BeforeEach
	public void create() {
		ProductRequest productRequest = Utils.generateNewProductRequest();
		sku = given()
        		.spec(requestSpec)
        		.body(productRequest)
        	.when()
        		.post("/api/v1/product/")
        	.then()
        		.statusCode(HttpStatus.SC_CREATED)
        		.body("status", equalTo(true))
        		.body("message", equalTo("El producto fue creado con éxito!"))
        		.body("sku", CoreMatchers.not(equalTo("")))
        		.body("sku", CoreMatchers.not(emptyString()))
        		.extract()
        		.jsonPath().getString("sku");
	}
	
	
	@Order(1)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_crear.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado - Crear nuevo producto usando /api/v1/product/")
    public void createNewProduct(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest(name,description,fPrice);
    	given()
    		.log().all()
    			.spec(requestSpec)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo(message))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.body("sku", CoreMatchers.not(emptyString()))
    		//.body("sku", equalTo(""))
    		.log()
    		.all();
    }
	
	@Order(2)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_actualizar.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado - Actualizar producto usando /api/v1/product/")
    public void updateProduct(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	
    	productRequest.setName(name);
    	productRequest.setDescription(description);
    	productRequest.setPrice(fPrice);
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.put("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_OK)
    	.body("status", equalTo(true))
    	.body("message", equalTo(message))
    	.log()
    	.all();
    }
	
	@Order(3)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_actualizar_precio.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado - Actualizar precio del producto usando /api/v1/product/")
    public void updateProductPrice(String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	
    	productRequest.setPrice(fPrice);
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.patch("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_OK)
    	.body("status", equalTo(true))
    	.body("message", equalTo(message))
    	.log()
    	.all();
    }
	
	@Order(4)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_eliminar.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado - Eliminar el producto usando /api/v1/product/")
    public void deleteProduct(String message) {
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.delete("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_OK)
    	.body("status", equalTo(true))
    	.body("message", equalTo(message))
    	.log()
    	.all();
    }
	
	//TEST NEGATIVOS
    
    // TEST NEGATIVO PARA POST CREATE SIN NAME 
	
	@Order(5)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_crear_sin_nombre.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado Negativo - Crear nuevo producto sin nombre usando /api/v1/product/")
    public void createNewProductwitoutName(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest(name,description,fPrice);
    	given()
    		.log().all()
    			.spec(requestSpec)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_BAD_REQUEST)
    		.body("status", equalTo(false))
    		.body("message", equalTo(message))
    		.body("sku", equalTo(""));
    }
	
    // TEST NEGATIVO PARA POST CREATE SIN DESCRIPCIÓN 
	
	@Order(6)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_crear_sin_descripcion.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado Negativo - Crear nuevo producto sin descripción usando /api/v1/product/")
    public void createNewProductwitoutDescription(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest(name,description,fPrice);
    	given()
    		.log().all()
    			.spec(requestSpec)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_BAD_REQUEST)
    		.body("status", equalTo(false))
    		.body("message", equalTo(message))
    		.body("sku", equalTo(""));
    }
	
	// TEST NEGATIVO PARA POST CREATE SIN PRECIO 
	
	@Order(7)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_crear_sin_precio.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado Negativo - Crear nuevo producto sin precio usando /api/v1/product/")
    public void createNewProductwitoutPrice(String name,String description, String price, String message) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest(name,description,fPrice);
    	given()
    		.log().all()
    			.spec(requestSpec)
    		 .body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_BAD_REQUEST)
    		.body("status", equalTo(false))
    		.body("message", equalTo(message))
    		.body("sku", equalTo(""));
    }
	
	// TEST NEGATIVO PARA PUT - PRODUCTO SIN SKU
	
	@Order(8)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_actualizar_sin_sku.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado Negativo - Actualizar producto usando /api/v1/product/")
    public void updateProductwithoutSKU(String name,String description, String price, String message, String sku) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	
    	productRequest.setName(name);
    	productRequest.setDescription(description);
    	productRequest.setPrice(fPrice);
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.put("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
    	.body("status", equalTo(405));
    }
	
	// TEST NEGATIVO PARA PUT - PRODUCTO NO ENCONTRADO
	
	@Order(9)
	@ParameterizedTest
	@CsvFileSource(resources = "/datos_actualizar_not_found.csv", numLinesToSkip = 1)
    @DisplayName("Parametrizado Negativo - Actualizar producto no encontrado usando /api/v1/product/")
    public void updateNotFoundProduct(String name,String description, String price, String message, String sku) {
		float fPrice = Float.parseFloat(price);
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	
    	productRequest.setName(name);
    	productRequest.setDescription(description);
    	productRequest.setPrice(fPrice);
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.put("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_BAD_REQUEST)
    	.body("status", equalTo(false))
    	.body("message", equalTo(message))
    	.log()
    	.all();
    }
	
	// TEST NEGATIVO PARA PUT - PRODUCTO ACTUALIZADO SIN NOMBRE
	
		@Order(10)
		@ParameterizedTest
		@CsvFileSource(resources = "/datos_actualizar_sin_nombre.csv", numLinesToSkip = 1)
	    @DisplayName("Parametrizado Negativo - Actualizar producto sin nombre usando /api/v1/product/")
	    public void updateProductwithoutName(String name,String description, String price, String message) {
			float fPrice = Float.parseFloat(price);
	    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
	    	
	    	productRequest.setName(name);
	    	productRequest.setDescription(description);
	    	productRequest.setPrice(fPrice);
	    	
	    	given()
			.pathParam("sku", sku)
	    	.contentType(ContentType.JSON)
	    	.body(productRequest)
	    .when()
	    	.put("/api/v1/product/{sku}/")
	    .then()
	    	.statusCode(HttpStatus.SC_BAD_REQUEST)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo(message))
	    	.log()
	    	.all();
	    }
	
	// TEST NEGATIVO PARA PUT - PRODUCTO ACTUALIZADO SIN DESCRIPCIÓN
		
		@Order(11)
		@ParameterizedTest
		@CsvFileSource(resources = "/datos_actualizar_sin_descripcion.csv", numLinesToSkip = 1)
		@DisplayName("Parametrizado Negativo - Actualizar producto sin nombre usando /api/v1/product/")
		public void updateProductwithoutDescription(String name,String description, String price, String message) {
			float fPrice = Float.parseFloat(price);
		    ProductRequest productRequest = Utils.generateNewProductRequest();    	
		    	
		    productRequest.setName(name);
		    productRequest.setDescription(description);
		    productRequest.setPrice(fPrice);
		    	
		    given()
				.pathParam("sku", sku)
		    	.contentType(ContentType.JSON)
		    	.body(productRequest)
		    .when()
		    	.put("/api/v1/product/{sku}/")
		    .then()
		    	.statusCode(HttpStatus.SC_BAD_REQUEST)
		    	.body("status", equalTo(false))
		    	.body("message", equalTo(message))
		    	.log()
		    	.all();
		    }
		
	// TEST NEGATIVO PARA PUT - PRODUCTO ACTUALIZADO SIN PRECIO
		
		@Order(12)
		@ParameterizedTest
		@CsvFileSource(resources = "/datos_actualizar_sin_precio.csv", numLinesToSkip = 1)
		@DisplayName("Parametrizado Negativo - Actualizar producto sin nombre usando /api/v1/product/")
		public void updateProductwithoutPrice(String name,String description, String price, String message) {
			float fPrice = Float.parseFloat(price);
		    ProductRequest productRequest = Utils.generateNewProductRequest();    	
		    	
		    productRequest.setName(name);
		    productRequest.setDescription(description);
		    productRequest.setPrice(fPrice);
		    	
		    given()
				.pathParam("sku", sku)
		    	.contentType(ContentType.JSON)
		    	.body(productRequest)
		    .when()
		    	.put("/api/v1/product/{sku}/")
		    .then()
		    	.statusCode(HttpStatus.SC_BAD_REQUEST)
		    	.body("status", equalTo(false))
		    	.body("message", equalTo(message))
		    	.log()
		    	.all();
		    }
		
	// TEST NEGATIVO PARA PATCH - PRODUCTO ACTUALIZADO CON PRECIO MAYOR A 0
		
		@Order(13)
		@ParameterizedTest
		@CsvFileSource(resources = "/datos_actualizar_mayor_cero.csv", numLinesToSkip = 1)
	    @DisplayName("Parametrizado Negativo - Actualizar precio del producto usando /api/v1/product/")
	    public void updateProductPriceEqualToZero(String name,String description, String price, String message) {
			float fPrice = Float.parseFloat(price);
	    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
	    	
	    	productRequest.setPrice(fPrice);
	    	
	    	given()
			.pathParam("sku", sku)
	    	.contentType(ContentType.JSON)
	    	.body(productRequest)
	    .when()
	    	.patch("/api/v1/product/{sku}/")
	    .then()
	    	.statusCode(HttpStatus.SC_BAD_REQUEST)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo(message))
	    	.log()
	    	.all();
	    }
		
	// TEST NEGATIVO PARA DELETE - PRODUCTO NO ENCONTRADO
		
		@Order(14)
		@ParameterizedTest
		@CsvFileSource(resources = "/datos_eliminar_no_encontrado.csv", numLinesToSkip = 1)
	    @DisplayName("Parametrizado Negativo - Eliminar el producto no encontrado usando /api/v1/product/")
	    public void deleteProductNotFound(String sku, String message) {
	    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
	    	
	    	given()
			.pathParam("sku", sku)
	    	.contentType(ContentType.JSON)
	    	.body(productRequest)
	    .when()
	    	.delete("/api/v1/product/{sku}/")
	    .then()
	    	.statusCode(HttpStatus.SC_NOT_FOUND)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo(message))
	    	.log()
	    	.all();
	    }	
			
	// TEST NEGATIVO PARA DELETE - SKU VACIO
		
		@Order(15)
		@ParameterizedTest
		@CsvFileSource(resources = "/datos_eliminar_sin_sku.csv", numLinesToSkip = 1)
	    @DisplayName("Parametrizado Negativo - Eliminar el producto no encontrado usando /api/v1/product/")
	    public void deleteProductwithoutSKU(String sku) {
	    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
	    	
	    	given()
			.pathParam("sku", sku)
	    	.contentType(ContentType.JSON)
	    	.body(productRequest)
	    .when()
	    	.delete("/api/v1/product/{sku}/")
	    .then()
	    	.statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
	    	.log()
	    	.all();
	    }			

	/*

	
	@Disabled
	@Test
    @DisplayName("Fallo - Actualizar precio  usando /api/v1/product/")
    public void updatePrice() {
    	ProductRequest productRequest = Utils.generateNewProductRequest();    	
    	productRequest.setName("No Modificar");
    	productRequest.setDescription("No Modificar");
    	productRequest.setPrice(0);
    	
    	given()
		.pathParam("sku", sku)
    	.contentType(ContentType.JSON)
    	.body(productRequest)
    .when()
    	.put("/api/v1/product/{sku}/")
    .then()
    	.statusCode(HttpStatus.SC_BAD_REQUEST)
    	.body("status", equalTo(false))
    	.body("message", equalTo(""))
    	.log()
    	.all();
    }
	
    @Test
    @DisplayName("Fallo - Eliminar producto usando /api/v1/product/")
    public void deleteProduct() {
    	String skuFalso="00000";
    	given()
        	.contentType(ContentType.JSON)
        .when()
        	.delete(String.format("/api/v1/product/%1$s/",skuFalso))
        .then()
	    	.statusCode(HttpStatus.SC_NOT_FOUND)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo("El producto no fue encontrado"))
	    	.log()
	    	.all();  	
    }
    
*/
}
