package com.qat.playwright.steps;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qat.playwright.BrowserLauncher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class ExampleSteps {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @Before
    public void setup() {
        playwright = Playwright.create();
    }

    @After
    public void teardown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Given("I launch the {string} browser")
    public void i_launch_the_browser(String browserName) {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        browser = BrowserLauncher.launch(playwright, browserName, headless);
        page = browser.newPage();
    }

    @When("I navigate to {string}")
    public void i_navigate_to(String url) {
        page.navigate(url);
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedTitle) {
        Assertions.assertTrue(page.title().contains(expectedTitle));
    }
}
