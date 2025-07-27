package com.interview.preperations;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredPracticeTest {

    private static String authToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

    // ---------------------------------------
    // Section: GET Requests
    // ---------------------------------------

    @Test(enabled = false)
    public void testGetUsers() {
        Response response = given()
                .log().uri()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().status()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getContentType().contains("application/json"));

        int userCount = response.jsonPath().getList("data").size();
        Assert.assertTrue(userCount > 0);

        String firstName = response.jsonPath().getString("data[2].first_name");
        Assert.assertNotNull(firstName);

        System.out.println("Extracted First Name: " + firstName);
    }

    // ---------------------------------------
    // Section: POST - Create User
    // ---------------------------------------

    @Test(enabled = false)
    public void testCreateUser() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "John");
        requestBody.put("job", "QA Engineer");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertNotNull(response.jsonPath().getString("id"));
        Assert.assertNotNull(response.jsonPath().getString("createdAt"));

        Assert.assertEquals(response.jsonPath().getString("name"), requestBody.get("name"));
        Assert.assertEquals(response.jsonPath().getString("job"), requestBody.get("job"));
    }

    // ---------------------------------------
    // Section: PUT - Update User (Full Update)
    // ---------------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PutPayload {
        private String name;
        private String job;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getJob() { return job; }
        public void setJob(String job) { this.job = job; }
    }

    @Test(enabled = false)
    public void testUpdateUser() {
        PutPayload payload = new PutPayload();
        payload.setName("John Updated");
        payload.setJob("Senior QA Engineer");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .body(payload)
                .when()
                .put("/api/users/2")
                .then()
                .extract()
                .response();

        PutPayload responseBody = response.as(PutPayload.class);

        Assert.assertEquals(responseBody.getName(), payload.getName());
        Assert.assertEquals(responseBody.getJob(), payload.getJob());
    }

    // ---------------------------------------
    // Section: PATCH - Partial Update
    // ---------------------------------------

    @Test(enabled = false)
    public void testPatchUser() {
        PutPayload patchPayload = new PutPayload();
        patchPayload.setName("Morpheus");
        patchPayload.setJob("Zion Resident");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .body(patchPayload)
                .when()
                .patch("/api/users/2")
                .then()
                .log().all()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200);

        PutPayload patchResponse = response.as(PutPayload.class);
        Assert.assertEquals(patchResponse.getName(), patchPayload.getName());
        Assert.assertEquals(patchResponse.getJob(), patchPayload.getJob());
    }

    // ---------------------------------------
    // Section: DELETE - Delete User
    // ---------------------------------------

    @Test(enabled = false)
    public void testDeleteUser() {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .when()
                .delete("/api/users/2")
                .then()
                .log().status()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 204);
        Assert.assertTrue(response.getBody().asString().isEmpty());
    }

    // ---------------------------------------
    // Section: Auth - Basic
    // ---------------------------------------

    @Test(enabled = false)
    public void testBasicAuth() {
        Response response = given()
                .auth().basic("postman", "password")
                .when()
                .get("https://postman-echo.com/basic-auth")
                .then()
                .log().all()
                .extract()
                .response();

        Assert.assertTrue(response.jsonPath().getBoolean("authenticated"));
    }

    // ---------------------------------------
    // Section: Auth - Bearer Token (Static)
    // ---------------------------------------

    @Test(enabled = false)
    public void testBearerToken() {
        Response response = given()
                .header("Authorization", "Bearer 123abc")
                .when()
                .get("/api/users/2")
                .then()
                .log().all()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    // ---------------------------------------
    // Section: Auth - Login, Extract Token, Use Token
    // ---------------------------------------

    @Test
    public void testLoginAndExtractToken() {
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", "eve.holt@reqres.in");
        loginPayload.put("password", "cityslicka");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .body(loginPayload)
                .when()
                .post("/api/login")
                .then()
                .log().all()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200);
        authToken = response.jsonPath().getString("token");
        Assert.assertNotNull(authToken, "Token is null");

        System.out.println("Extracted Token: " + authToken);
    }

    @Test(dependsOnMethods = "testLoginAndExtractToken")
    public void testAuthorizedGetWithToken() {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/users/2")
                .then()
                .log().all()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200);
        String firstName = response.jsonPath().getString("data.first_name");
        Assert.assertNotNull(firstName);
        System.out.println("First Name from Authorized GET: " + firstName);
    }
}
