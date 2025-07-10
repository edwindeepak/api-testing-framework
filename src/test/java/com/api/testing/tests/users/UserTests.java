package com.api.testing.tests.users;


import com.api.testing.base.RequestSpecFactory;
import com.api.testing.models.user.UserResponse;
import com.api.testing.tests.base.BaseTest;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UserTests extends BaseTest {

    @Test
    public void testGetUserById() {
        String endpoint = "/api/users/2";

        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())  // inherited from BaseTest with baseURI, headers etc
                .when()
                .get(endpoint)
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response();

        // Deserialize response to POJO
        UserResponse userResponse = response.as(UserResponse.class);

        // Validate some fields
        Assert.assertEquals(userResponse.getData().getId(), 2);
        Assert.assertEquals(userResponse.getData().getEmail(), "janet.weaver@reqres.in");

        // Optionally print the response for debug
        System.out.println("User: " + userResponse.getData().getFirst_name() + " " + userResponse.getData().getLast_name());
    }
}
