package com.api.testing.base;

import com.api.testing.config.EnvConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecFactory {

    private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();

    public static RequestSpecification getRequestSpec() {
        if (requestSpec.get() == null) {
            requestSpec.set(new RequestSpecBuilder()
                .setBaseUri(EnvConfigReader.get("base.uri"))
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", EnvConfigReader.get("api.key"))  // Add API key header here
                .build());
        }
        return requestSpec.get();
    }

    public static void removeRequestSpec() {
        requestSpec.remove();
    }
}
