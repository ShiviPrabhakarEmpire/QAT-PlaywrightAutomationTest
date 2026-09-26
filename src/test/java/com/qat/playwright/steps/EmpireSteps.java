package com.qat.playwright.steps;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qat.playwright.BrowserLauncher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class EmpireSteps {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @Before
    public void setup() {
        playwright = Playwright.create();
    }

    @After
    public void teardown() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Given("I launch the browser")
    public void i_launch_the_browser() {
        String browserName = System.getProperty("browser", "chromium");
        String deviceName = System.getProperty("device", "Desktop");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        
        browser = BrowserLauncher.launch(playwright, browserName, headless);
        
        if ("Desktop".equalsIgnoreCase(deviceName)) {
            context = browser.newContext();
        } else {
            Browser.NewContextOptions deviceOptions = new Browser.NewContextOptions();
            if ("iPhone 13".equals(deviceName) || "iPhone_13".equalsIgnoreCase(deviceName)) {
                deviceOptions.setViewportSize(390, 844)
                             .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1")
                             .setDeviceScaleFactor(3)
                             .setIsMobile(true)
                             .setHasTouch(true);
            } else if ("Pixel 5".equals(deviceName) || "Pixel_5".equalsIgnoreCase(deviceName)) {
                deviceOptions.setViewportSize(393, 851)
                             .setUserAgent("Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.91 Mobile Safari/537.36")
                             .setDeviceScaleFactor(2.75)
                             .setIsMobile(true)
                             .setHasTouch(true);
            } else {
                throw new IllegalArgumentException("Unsupported device: " + deviceName);
            }
            context = browser.newContext(deviceOptions);
        }
        page = context.newPage();
    }

    @When("I navigate to the Empire page {string}")
    public void i_navigate_to(String url) {
        page.navigate(url);
    }

    @Then("the Empire page title should contain {string}")
    public void the_page_title_should_contain(String expectedTitle) {
        Assertions.assertTrue(page.title().contains(expectedTitle));
    }

    // --- New Empire.ca functional steps ---

    private void openMobileMenuIfPresent() {
        if (page.locator(".js-hamburger").first().isVisible()) {
            page.locator(".js-hamburger").first().click();
            page.waitForTimeout(1000); // Wait for mobile menu animation
        }
    }

    @When("I click on the {string} link")
    public void i_click_on_the_link(String linkText) {
        openMobileMenuIfPresent();
        // Force click is used because Empire's mega-menu links might be hidden 
        // under dropdown wrappers depending on viewport or hover state.
        page.locator("text=" + linkText).first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
    }

    @Then("I should be redirected to a page containing {string}")
    public void i_should_be_redirected_to_a_page_containing(String expectedPath) {
        page.waitForURL("**" + expectedPath + "**");
        Assertions.assertTrue(page.url().contains(expectedPath));
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        openMobileMenuIfPresent();
        // On mobile, the search might be inside the menu or toggled differently, force it.
        page.locator("input[name='search']").first().fill(searchTerm);
        page.locator("input#edit-submit-acquia-search").first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
    }

    @Then("the search results page should be displayed")
    public void the_search_results_page_should_be_displayed() {
        page.waitForURL("**/search**");
        Assertions.assertTrue(page.url().contains("search"));
    }

    @When("I click the Log in button")
    public void i_click_the_log_in_button() {
        openMobileMenuIfPresent();
        page.locator("button:has-text('Log in'), a:has-text('Log in'), button:has-text('Se connecter'), a:has-text('Se connecter')").first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
    }

    @When("I select {string} from the dropdown")
    public void i_select_from_the_dropdown(String optionText) {
        page.locator("text=" + optionText).first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
    }

    @Then("a new tab should open pointing to the MyEmpire portal")
    public void a_new_tab_should_open_pointing_to_the_myempire_portal() {
        // Find the newly opened page by looking for the one with the correct URL,
        // or just asserting that any page in the context contains the target URL
        page.waitForTimeout(3000); 
        boolean found = false;
        for (Page p : context.pages()) {
            if (p.url().contains("my.empire.ca")) {
                found = true;
                break;
            }
        }
        Assertions.assertTrue(found, "No new tab was opened pointing to my.empire.ca");
    }
}
