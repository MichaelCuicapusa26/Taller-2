package com.jbenterprise.rest_assured.utils;

import com.jbenterprise.rest_assured.entity.ProductRequest;

public class Utils {
	public static final String USER_AGENT="JBEnterprise";
	
    public static ProductRequest generateNewProductRequest() {
    	return ProductRequest.builder()
    			.name("Iphone 15")
    			.description("Un equipo moderno de la marca Apple")
    			.price(1400)
    			.build();    	
    }
    
    public static ProductRequest generateNewProductRequest(String name,String description, float price) {
    	return ProductRequest.builder()
    			.name(name)
    			.description(description)
    			.price(price)
    			.build();    	
    }  
    
    //UTILIZADO PARA LOS CASOS NEGATIVOS:
    
    public static ProductRequest generateNewProductRequestwithoutName() {
    	return ProductRequest.builder()
    			.name("")
    			.description("Un equipo moderno de la marca Apple")
    			.price(1400)
    			.build();    	
    }
    
    public static ProductRequest generateNewProductRequestwithoutName(String name,String description, float price) {
    	return ProductRequest.builder()
    			.name(name)
    			.description(description)
    			.price(price)
    			.build();    	
    }  
    
    public static ProductRequest generateNewProductRequestwithoutDescription() {
    	return ProductRequest.builder()
    			.name("Iphone 15")
    			.description("")
    			.price(1400)
    			.build();    	
    }
    
    public static ProductRequest generateNewProductRequestwithoutDescription(String name,String description, float price) {
    	return ProductRequest.builder()
    			.name(name)
    			.description(description)
    			.price(price)
    			.build();    	
    }  
    
    
    public static ProductRequest generateNewProductRequestwithoutPrice() {
    	return ProductRequest.builder()
    			.name("Iphone 15")
    			.description("Un equipo moderno de la marca Apple")
    			.price(0)
    			.build();    	
    }
    
    public static ProductRequest generateNewProductRequestwithoutPrice(String name,String description, float price) {
    	return ProductRequest.builder()
    			.name(name)
    			.description(description)
    			.price(price)
    			.build();    	
    }  
}
