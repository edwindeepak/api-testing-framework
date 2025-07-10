package com.api.testing.tests.login;

import com.api.testing.base.RequestSpecFactory;
import com.api.testing.models.login.LoginRequest;
import com.api.testing.models.login.LoginResponse;
import com.api.testing.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "validLoginData", dataProviderClass = com.api.testing.dataproviders.LoginDataProvider.class)
    public void testSuccessfulLogin(String email, String password) {
        log.info("Starting login test with email: " + email);

        // Use POJO as request body
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Send request
        Response response = given()
                .spec(RequestSpecFactory.getRequestSpec())
                .body(loginRequest)
                .when()
                .post("/api/login")
                .then()
                .log().all()
                .extract()
                .response();

        // Deserialize response to POJO
        LoginResponse loginResponse = response.as(LoginResponse.class);

        log.info("Response status code: " + response.statusCode());
        log.info("Response token: " + loginResponse.getToken());

        // Assertions
        Assert.assertEquals(response.statusCode(), 200, "Expected HTTP status 200");
        Assert.assertNotNull(loginResponse.getToken(), "Token should not be null");
    }
}
