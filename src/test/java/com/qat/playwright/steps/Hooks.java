package com.qat.playwright.steps;

import com.microsoft.playwright.Playwright;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before("not @parity")
    public void setup() {
        testContext.playwright = Playwright.create();
    }

    @After("not @parity")
    public void teardown() {
        if (testContext.context != null) {
            testContext.context.close();
        }
        if (testContext.browser != null) {
            testContext.browser.close();
        }
        if (testContext.playwright != null) {
            testContext.playwright.close();
        }
    }
}
