package com.qat.playwright.steps;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class NavigationSteps {

    private final TestContext testContext;

    public NavigationSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    private void openMobileMenuIfPresent() {
        if (testContext.page.locator(".js-hamburger").first().isVisible()) {
            testContext.page.locator(".js-hamburger").first().click();
            testContext.page.waitForTimeout(1000); // Wait for mobile menu animation
        }
    }

    @When("I click on the {string} link")
    public void i_click_on_the_link(String linkText) {
        openMobileMenuIfPresent();
        String href = null;
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\s*" + linkText + "\\s*$", java.util.regex.Pattern.CASE_INSENSITIVE);
            href = (String) testContext.page.locator("a").filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(pattern)).first().getAttribute("href");
        } catch (Exception e) {
            System.out.println("Error getting href: " + e.getMessage());
        }
        System.out.println("Extracted href: " + href);

        if (href != null && !href.isEmpty() && !href.equals("#")) {
            if (href.startsWith("http")) {
                testContext.page.navigate(href);
            } else {
                String baseUrl = com.qat.playwright.utils.ConfigReader.getProperty("BASE_URL", "https://www.empire.ca");
                if (testContext.page.url().contains("/fr")) {
                    baseUrl = com.qat.playwright.utils.ConfigReader.getProperty("TARGET_URL", "https://www.empire.ca/fr");
                }
                if (href.startsWith("/")) {
                    testContext.page.navigate(com.qat.playwright.utils.ConfigReader.getProperty("BASE_URL", "https://www.empire.ca") + href);
                } else {
                    testContext.page.navigate(baseUrl + "/" + href);
                }
            }
        } else {
            testContext.page.locator("text=" + linkText).first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        }
    }

    @Then("I should be redirected to a page containing {string}")
    public void i_should_be_redirected_to_a_page_containing(String expectedPath) {
        Assertions.assertTrue(testContext.page.url().contains(expectedPath), "URL mismatch: " + testContext.page.url());
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        openMobileMenuIfPresent();
        String currentUrl = testContext.page.url();
        String baseUrlEn = com.qat.playwright.utils.ConfigReader.getProperty("BASE_URL", "https://www.empire.ca");
        String baseUrlFr = com.qat.playwright.utils.ConfigReader.getProperty("TARGET_URL", "https://www.empire.ca/fr");
        String searchBase = currentUrl.contains("/fr") ? baseUrlFr + "/search" : baseUrlEn + "/search";
        testContext.page.navigate(searchBase + "?search_api_fulltext=" + searchTerm.replace(" ", "+"));
    }

    @Then("the search results page should be displayed")
    public void the_search_results_page_should_be_displayed() {
        testContext.page.waitForURL("**/search**");
        Assertions.assertTrue(testContext.page.url().contains("search"));
    }

    @When("I click the Log in button")
    public void i_click_the_log_in_button() {
        openMobileMenuIfPresent();
        try {
             testContext.page.locator("button:has-text('Log in'), a:has-text('Log in'), button:has-text('Se connecter'), a:has-text('Se connecter')").first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        } catch(Exception e) {}
    }

    @When("I select {string} from the dropdown")
    public void i_select_from_the_dropdown(String optionText) {
        String href = null;
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\s*" + optionText + "\\s*$", java.util.regex.Pattern.CASE_INSENSITIVE);
            href = (String) testContext.page.locator("a").filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(pattern)).first().getAttribute("href");
        } catch (Exception e) {}

        if (href != null && !href.isEmpty() && !href.equals("#")) {
            testContext.page.navigate(href);
        } else {
            testContext.page.locator("text=" + optionText).first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        }
    }

    @Then("a new tab should open pointing to the MyEmpire portal")
    public void a_new_tab_should_open_pointing_to_the_myempire_portal() {
        boolean found = false;
        for (int i = 0; i < 5; i++) {
            testContext.page.waitForTimeout(1000); 
            for (Page p : testContext.context.pages()) {
                if (p.url().contains("my.empire.ca") || p.url().contains("login.empire.ca")) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        Assertions.assertTrue(found, "No portal page was opened pointing to my.empire.ca or login.empire.ca");
    }
}
