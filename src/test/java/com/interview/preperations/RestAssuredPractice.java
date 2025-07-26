package com.interview.preperations;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;

import org.testng.ITestContext;
import org.testng.annotations.Test;

public class RestAssuredPractice {
	
	
	
	@Test(enabled=true, priority = 1)
	public void simpletest() {
		
		given().baseUri("https://jsonplaceholder.typicode.com")
		.when().get("/posts/1")
		.then().statusCode(200).body("userId",equalTo(1)).log().all();
		
	}
	
	@Test(enabled=true,  priority = 2)
	public void testtokenBearer() {
		String token = "asdefr";
		Response response = given().baseUri("https://reqres.in/api/users/2").
				header("Authorization","Bearer "+token)
				.when().get().then().extract().response();
		
		 System.out.println("Response body: " + response.getBody().asString());
		 
		 response.then().statusCode(200);
	}
	
	@Test(priority = 3,enabled=true)
	public void getTokenPost(ITestContext context) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email","eve.holt@reqres.in");
		map.put("password","cityslicka");
		
		Response response = given().baseUri("https://reqres.in").
				header("Content-Type", "application/json")
				.header("x-api-key","reqres-free-v1")
				.body(map).
				when().post("/api/login");
		
		System.out.println(response.getBody().asString());
		
		context.setAttribute("token", response.jsonPath().getString("token"));	
		
		
		
				
	}
	
	@Test(dependsOnMethods = "getTokenPost", enabled=true, priority = 4)
	public void getuserswithToken(ITestContext context) {
		Response response = given().baseUri("https://reqres.in")
				.headers("Authorization" ,"Bearer "+context.getAttribute("token"))
				.when().get("/api/users/2");
		response.then().log().all();
		System.out.println(response.body().asPrettyString());
	}
	
}
