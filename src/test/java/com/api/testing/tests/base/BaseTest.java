package com.api.testing.tests.base;

import com.api.testing.base.RequestSpecFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setup() {
        log.info("------ Test Started ------");
    }

    @AfterMethod
    public void teardown() {
        RequestSpecFactory.removeRequestSpec();
        log.info("------ Test Finished ------");
    }
}
