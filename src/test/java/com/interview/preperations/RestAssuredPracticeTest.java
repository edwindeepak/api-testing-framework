package com.interview.preperations;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

public class RestAssuredPracticeTest {

    @Test
    public void testGetUsers() {
        RestAssured.baseURI = "https://reqres.in";

        Response response = given()
                                .log().uri()
                            .when()
                                .get("/api/users?page=2")
                            .then()
                                .log().status()
                                .extract()
                                .response();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch");
        Assert.assertTrue(response.getContentType().contains("application/json"), "Unexpected content type");

        int userCount = response.jsonPath().getList("data").size();
        Assert.assertTrue(userCount > 0, "User list is empty");

        String firstName = response.jsonPath().getString("data[2].first_name");
        Assert.assertNotNull(firstName, "First name is null");

        System.out.println("Extracted First Name: " + firstName);
    }

    @Test
    public void testCreateUser() {
        RestAssured.baseURI = "https://reqres.in";

        Map<String, String> data = new HashMap<>();
        data.put("name", "John");
        data.put("job", "QA Engineer");

        Response response = given()
                                .contentType(ContentType.JSON)
                                .header("x-api-key", "reqres-free-v1")  // Optional for ReqRes
                                .body(data)
                            .when()
                                .post("/api/users")
                            .then()
                                .extract()
                                .response();

        System.out.println(response.asString());

        Assert.assertEquals(response.statusCode(), 201, "Unexpected status code");

        String responseID = response.jsonPath().getString("id");
        String responseCreationTime = response.jsonPath().getString("createdAt");

        Assert.assertNotNull(responseID, "ID should not be null");
        Assert.assertNotNull(responseCreationTime, "createdAt should not be null");

        Assert.assertEquals(response.jsonPath().getString("name"), data.get("name"));
        Assert.assertEquals(response.jsonPath().getString("job"), data.get("job"));
    }

    @Test
    public void testUpdateUser() {
        RestAssured.baseURI = "https://reqres.in";

        PutPayload payload = new PutPayload();
        payload.setName("John Updated");
        payload.setJob("Senior QA Engineer");

        Response response = given()
                                .contentType(ContentType.JSON)
                                .header("x-api-key", "reqres-free-v1")  // Optional
                                .body(payload)
                            .when()
                                .put("/api/users/2")
                            .then()
                                .extract()
                                .response();

        System.out.println(response.asString());

        PutPayload responseBody = response.as(PutPayload.class);

        Assert.assertEquals(responseBody.getName(), payload.getName(), "Name mismatch in response");
        Assert.assertEquals(responseBody.getJob(), payload.getJob(), "Job mismatch in response");

        System.out.println("Updated Name: " + responseBody.getName());
        System.out.println("Updated Job: " + responseBody.getJob());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PutPayload {
        private String name;
        private String job;

        public PutPayload() {
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getJob() {
            return job;
        }
        public void setJob(String job) {
            this.job = job;
        }
    }
}
