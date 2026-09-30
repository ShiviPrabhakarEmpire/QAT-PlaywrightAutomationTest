package com.qat.playwright.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class FindAdvisorSteps {

    private final TestContext testContext;

    public FindAdvisorSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I fill out the Find an Advisor form with my contact details")
    public void i_fill_out_the_find_an_advisor_form_with_my_contact_details() {
        testContext.page.fill("#id_first_name", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_FIRST_NAME", "Test"));
        testContext.page.fill("#id_last_name", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_LAST_NAME", "Automation"));
        testContext.page.fill("#id_phone", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_PHONE", "4165551234"));
        testContext.page.fill("#id_postal_code", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_POSTAL_CODE", "M5V 2H1"));
        testContext.page.fill("#id_city", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_CITY", "Toronto"));
        testContext.page.selectOption("#id_province", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_PROVINCE", "ON"));
        testContext.page.fill("#id_email", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_EMAIL", "test.automation@example.com"));
        testContext.page.fill("#id_verify_email", com.qat.playwright.utils.ConfigReader.getProperty("FIND_ADVISOR_EMAIL", "test.automation@example.com"));
    }

    @When("I consent to the use of my personal information")
    public void i_consent_to_the_use_of_my_personal_information() {
        testContext.page.locator("#id_agree").check(new com.microsoft.playwright.Locator.CheckOptions().setForce(true));
    }

    @Then("the Search Now button should be enabled")
    public void the_search_now_button_should_be_enabled() {
        com.microsoft.playwright.Locator submitButton = testContext.page.locator("#edit-search.form-submit");
        Assertions.assertTrue(submitButton.isVisible(), "Search Now button is not visible");
        // We do not actually submit the form to avoid spamming the production system.
    }
}
