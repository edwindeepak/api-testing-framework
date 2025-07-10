package com.api.testing.dataproviders;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        return new Object[][] {
            {"eve.holt@reqres.in", "cityslicka"},
            {"another.user@reqres.in", "cityslicka"},
            {"test.user@reqres.in", "cityslicka"}
        };
    }
}
