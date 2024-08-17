package com.jbenterprise.rest_assured.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.utils.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class IntermediateTest {
	
	private static RequestSpecification requestSpec;

	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8084";
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("User-Agent", Utils.USER_AGENT);
        builder.addHeader("Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk");
        requestSpec = builder.build();
    }   
    
    @Test
    @DisplayName("Create new product using POST - /api/v1/product/")
    public void createNewProduct() {
    	 System.out.println("START - Running IntermediateSuite createNewProduct - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	given()
    		.spec(requestSpec)
    		.header("Content-Type",ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")));
    		//.log()
    		//.all();
    	System.out.println("END - Running IntermediateSuite createNewProduct - " + Thread.currentThread().getId());
    }
    
    @Test
    @DisplayName("Update product using PUT - /api/v1/product/")
    public void updateProduct() {
    	 System.out.println("START - Running IntermediateSuite updateProduct - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON,"Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk")
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.extract()
    		.jsonPath().getString("sku");

    	productRequest.setName("Name Updated");
    	productRequest.setDescription("Description Updated");
    	productRequest.setPrice(1900);

    	given()
    		.pathParam("sku", sku)
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.put("/api/v1/product/{sku}/")
        .then()
	    	.statusCode(HttpStatus.SC_OK)
	    	.body("status", equalTo(true))
	    	.body("message", equalTo("El producto fue actualizado con éxito"));
        	//.log()
        	//.all();
    	System.out.println("END - Running IntermediateSuite updateProduct - " + Thread.currentThread().getId());
    }
    
    @Test
    @DisplayName("Update product price using PATCH - /api/v1/product/")
    public void updateProductPrice() {
    	 System.out.println("START - Running IntermediateSuite updateProductPrice - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    			.spec(requestSpec)
    		.headers( "Content-Type", ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.extract()
    		.jsonPath().getString("sku");
    	
    	productRequest.setPrice(2500);

    	given()
    		.contentType(ContentType.JSON)
    		.body(productRequest)
    	.when()
        	.patch(String.format("/api/v1/product/%1$s/",sku))
        .then()
	    	.statusCode(HttpStatus.SC_OK)
	    	.body("status", equalTo(true))
	    	.body("message", equalTo("El precio del producto fue actualizado con éxito"));
	    	//.log()
	    	//.all();
    	System.out.println("END - Running IntermediateSuite updateProductPrice - " + Thread.currentThread().getId());
    }
    
    @Test
    @DisplayName("Delete product using DELETE - /api/v1/product/")
    public void deleteProduct() {
    	 System.out.println("START - Running IntermediateSuite deleteProduct - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.contentType(ContentType.JSON)
    		.spec(requestSpec)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
			.statusCode(HttpStatus.SC_CREATED)
			.body("status", equalTo(true))
			.body("message", equalTo("El producto fue creado con éxito!"))
			.body("sku", CoreMatchers.not(equalTo("")))
    		.extract()
    		.jsonPath().getString("sku");

    	given()
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.delete(String.format("/api/v1/product/%1$s/",sku))
        .then()
	    	.statusCode(HttpStatus.SC_OK)
	    	.body("status", equalTo(true))
	    	.body("message", equalTo("El producto fue eliminado con éxito"));
	    	//.log()
	    	//.all();
    	System.out.println("END - Running IntermediateSuite deleteProduct - " + Thread.currentThread().getId());
    }
    
    //TEST NEGATIVOS
    
    // TEST NEGATIVO PARA POST CREATE SIN NAME 
    
    @Test
    @DisplayName("Negative test - Create new product without name using POST - /api/v1/product/")
    public void createNewProductwithoutName() {
    	 System.out.println("START - Running IntermediateSuite createNewProductwithoutName - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequestwithoutName();
    	given()
    		.spec(requestSpec)
    		.header("Content-Type",ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_BAD_REQUEST)
    		.body("status", equalTo(false))
    		.body("message", equalTo("El nombre del producto no fue proporcionado"))
    		.body("sku", equalTo(""));
    		//.log()
    		//.all();
    	System.out.println("END - Running IntermediateSuite createNewProductwithoutName - " + Thread.currentThread().getId());
    }
    

    // TEST NEGATIVO PARA POST CREATE SIN DESCRIPCIÓN 
    
    @Test
    @DisplayName("Negative test - Create new product without description using POST - /api/v1/product/")
    public void createNewProductwithoutDescription() {
    	 System.out.println("START - Running IntermediateSuite createNewProductwithoutDescription - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequestwithoutDescription();
    	given()
    		.spec(requestSpec)
    		.header("Content-Type",ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_BAD_REQUEST)
    		.body("status", equalTo(false))
    		.body("message", equalTo("La descripción del producto no fue proporcionada"))
    		.body("sku", equalTo(""));
    		//.log()
    		//.all();
    	System.out.println("END - Running IntermediateSuite createNewProductwithoutDescription - " + Thread.currentThread().getId());
    }
    
    
// TEST NEGATIVO PARA POST CREATE SIN PRECIO 
    
    @Test
    @DisplayName("Negative test - Create new product without price using POST - /api/v1/product/")
    public void createNewProductwithoutPrice() {
    	 System.out.println("START - Running IntermediateSuite createNewProductwithoutPrice - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequestwithoutPrice();
    	given()
    		.spec(requestSpec)
    		.header("Content-Type",ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_BAD_REQUEST)
    		.body("status", equalTo(false))
    		.body("message", equalTo("El precio del producto no fue proporcionado"))
    		.body("sku", equalTo(""));
    		//.log()
    		//.all();
    	System.out.println("END - Running IntermediateSuite createNewProductwithoutPrice - " + Thread.currentThread().getId());
    }
    
// TEST NEGATIVO PARA PUT - PRODUCTO SIN SKU
    
    @Test
    @DisplayName("Negative test - Update product using PUT for a product without SKU - /api/v1/product/")
    public void updateProductwithoutSKU() {
    	 System.out.println("START - Running IntermediateSuite updateProductwithoutSKU - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = "";

    	productRequest.setName("Name Updated");
    	productRequest.setDescription("Description Updated");
    	productRequest.setPrice(1900);

    	given()
    		.pathParam("sku", sku)
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.put("/api/v1/product/{sku}/")
        .then()
	    	.statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
	    	.body("status", equalTo(405));
        	//.log()
        	//.all();
    	System.out.println("END - Running IntermediateSuite updateProductwithoutSKU - " + Thread.currentThread().getId());
    }
    
// TEST NEGATIVO PARA PUT - PRODUCTO NO ENCONTRADO
    
    @Test
    @DisplayName("Negative test - Update product using PUT for a not found product - /api/v1/product/")
    public void updateNotFoundProduct() {
    	 System.out.println("START - Running IntermediateSuite updateNotFoundProduct - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = "notvalidsku";

    	productRequest.setName("Name Updated");
    	productRequest.setDescription("Description Updated");
    	productRequest.setPrice(1900);

    	given()
    		.pathParam("sku", sku)
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.put("/api/v1/product/{sku}/")
        .then()
	    	.statusCode(HttpStatus.SC_BAD_REQUEST)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo("El producto no fue encontrado"));
        	//.log()
        	//.all();
    	System.out.println("END - Running IntermediateSuite updateNotFoundProduct - " + Thread.currentThread().getId());
    }
    
    // TEST NEGATIVO PARA PUT - PRODUCTO ACTUALIZADO SIN NOMBRE
    
    @Test
    @DisplayName("Negative test - Update product using PUT without Name - /api/v1/product/")
    public void updateProductwithoutName() {
    	 System.out.println("START - Running IntermediateSuite updateProductwithoutName - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON,"Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk")
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.extract()
    		.jsonPath().getString("sku");

    	productRequest.setName("");
    	productRequest.setDescription("Description Updated");
    	productRequest.setPrice(1900);

    	given()
    		.pathParam("sku", sku)
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.put("/api/v1/product/{sku}/")
        .then()
	    	.statusCode(HttpStatus.SC_BAD_REQUEST)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo("El nombre del producto no fue proporcionado"));
        	//.log()
        	//.all();
    	System.out.println("END - Running IntermediateSuite updateProductwithoutName - " + Thread.currentThread().getId());
    }
    
   // TEST NEGATIVO PARA PUT - PRODUCTO ACTUALIZADO SIN DESCRIPCIÓN
    
    @Test
    @DisplayName("Negative test - Update product using PUT without Description - /api/v1/product/")
    public void updateProductwithoutDescription() {
    	 System.out.println("START - Running IntermediateSuite updateProductwithoutDescription - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON,"Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk")
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.extract()
    		.jsonPath().getString("sku");

    	productRequest.setName("Name Updated");
    	productRequest.setDescription("");
    	productRequest.setPrice(1900);

    	given()
    		.pathParam("sku", sku)
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.put("/api/v1/product/{sku}/")
        .then()
	    	.statusCode(HttpStatus.SC_BAD_REQUEST)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo("La descripción del producto no fue proporcionada"));
        	//.log()
        	//.all();
    	System.out.println("END - Running IntermediateSuite updateProductwithoutDescription - " + Thread.currentThread().getId());
    }
    
    
   // TEST NEGATIVO PARA PUT - PRODUCTO ACTUALIZADO SIN PRECIO
    
    @Test
    @DisplayName("Negative test - Update product using PUT without Price - /api/v1/product/")
    public void updateProductwithoutPrice() {
    	 System.out.println("START - Running IntermediateSuite updateProductwithoutPrice - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON,"Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk")
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.extract()
    		.jsonPath().getString("sku");

    	productRequest.setName("Name Updated");
    	productRequest.setDescription("Description Updated");
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
	    	.body("message", equalTo("El precio del producto no fue proporcionado"));
        	//.log()
        	//.all();
    	System.out.println("END - Running IntermediateSuite updateProductwithoutPrice - " + Thread.currentThread().getId());
    }
    
    // TEST NEGATIVO PARA PATCH - PRODUCTO ACTUALIZADO CON PRECIO MAYOR A 0
    
    @Test
    @DisplayName("Negative test - Update product price using PATCH with a price equal to zero - /api/v1/product/")
    public void updateProductPriceEqualToZero() {
    	 System.out.println("START - Running IntermediateSuite updateProductPriceEqualToZero - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = given()
    			.spec(requestSpec)
    		.headers( "Content-Type", ContentType.JSON)
    		.body(productRequest)
    	.when()
    		.post("/api/v1/product/")
    	.then()
    		.statusCode(HttpStatus.SC_CREATED)
    		.body("status", equalTo(true))
    		.body("message", equalTo("El producto fue creado con éxito!"))
    		.body("sku", CoreMatchers.not(equalTo("")))
    		.extract()
    		.jsonPath().getString("sku");
    	
    	productRequest.setName("LORE_IPSUM");
    	productRequest.setDescription("LORE_IPSUM");
    	productRequest.setPrice(0);

    	given()
    		.contentType(ContentType.JSON)
    		.body(productRequest)
    	.when()
        	.patch(String.format("/api/v1/product/%1$s/",sku))
        .then()
	    	.statusCode(HttpStatus.SC_BAD_REQUEST)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo("El precio del producto debe ser mayor a 0"));
	    	//.log()
	    	//.all();
    	System.out.println("END - Running IntermediateSuite updateProductPriceEqualToZero - " + Thread.currentThread().getId());
    }
    
    // TEST NEGATIVO PARA DELETE - PRODUCTO NO ENCONTRADO
    
    @Test
    @DisplayName("Delete product using DELETE with a not found product - /api/v1/product/")
    public void deleteProductNotFound() {
    	 System.out.println("START - Running IntermediateSuite deleteProductNotFound - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = "aa";

    	given()
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.delete(String.format("/api/v1/product/%1$s/",sku))
        .then()
	    	.statusCode(HttpStatus.SC_NOT_FOUND)
	    	.body("status", equalTo(false))
	    	.body("message", equalTo("El producto no fue encontrado"));
	    	//.log()
	    	//.all();
    	System.out.println("END - Running IntermediateSuite deleteProductNotFound - " + Thread.currentThread().getId());
    }
    
    
    // TEST NEGATIVO PARA DELETE - SKU VACIO
    
    @Test
    @DisplayName("Negative test - Delete product without SKU using DELETE - /api/v1/product/")
    public void deleteProductwithoutSKU() {
    	 System.out.println("START - Running IntermediateSuite deleteProductwithoutSKU - " + Thread.currentThread().getId());
    	ProductRequest productRequest = Utils.generateNewProductRequest();
    	String sku = "";

    	given()
        	.contentType(ContentType.JSON)
        	.body(productRequest)
        .when()
        	.delete(String.format("/api/v1/product/%1$s/",sku))
        .then()
	    	.statusCode(HttpStatus.SC_BAD_REQUEST);
	    	//.log()
	    	//.all();
    	System.out.println("END - Running IntermediateSuite deleteProductwithoutSKU - " + Thread.currentThread().getId());
    }
    
}
