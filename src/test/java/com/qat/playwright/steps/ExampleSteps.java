package com.qat.playwright.steps;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.qat.playwright.BrowserLauncher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ExampleSteps {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    // Declare Page Object instances

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
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
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

    @Given("^Add (.*?) and (.*?)$")
    public void add_username_and_password_new(String username, String password) {
        page.locator("[name='username']").fill(username);
        page.locator("button[type='submit']").click();
        page.locator("[name='password']").fill(password);
        page.locator("button[type='submit']").click();
    }

    @Then("Home page should contain {string}")
    public void homepage_should_contain(String expectedTitle) {
        //page.waitForLoadState(LoadState.NETWORKIDLE);
        assertThat(page.locator("#home-breadcrumbs"))
                .hasText(expectedTitle, new com.microsoft.playwright.assertions.LocatorAssertions.HasTextOptions().setTimeout(10000));
    }
}