package com.qat.playwright.steps;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.qat.playwright.BrowserLauncher;
import com.qat.playwright.parity.DomParityService;
import com.qat.playwright.parity.VisualParityService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ParitySteps {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext baseContext;
    private BrowserContext targetContext;
    private Page basePage;
    private Page targetPage;
    private String testName;

    @Before("@parity")
    public void setupParity() {
        playwright = Playwright.create();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        String browserName = System.getProperty("browser", "chromium");
        browser = BrowserLauncher.launch(playwright, browserName, headless);

        String deviceName = System.getProperty("device", "Desktop");
        Browser.NewContextOptions options = new Browser.NewContextOptions();

        if ("Desktop".equalsIgnoreCase(deviceName)) {
            options.setViewportSize(1920, 1080);
            options.setDeviceScaleFactor(1.0);
        } else {
            try {
                java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("devices.json");
                if (is != null) {
                    try (java.io.InputStreamReader reader = new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8)) {
                        com.google.gson.JsonObject devices = com.google.gson.JsonParser.parseReader(reader).getAsJsonObject();
                        String searchKey = deviceName.replace("_", " ");
                        if (devices.has(searchKey)) {
                            com.google.gson.JsonObject d = devices.getAsJsonObject(searchKey);
                            options.setUserAgent(d.get("userAgent").getAsString());
                            options.setViewportSize(d.getAsJsonObject("viewport").get("width").getAsInt(), d.getAsJsonObject("viewport").get("height").getAsInt());
                            options.setDeviceScaleFactor(d.get("deviceScaleFactor").getAsDouble());
                            options.setHasTouch(d.get("hasTouch").getAsBoolean());
                            if (!"firefox".equalsIgnoreCase(System.getProperty("browser", "chromium"))) {
                                options.setIsMobile(d.get("isMobile").getAsBoolean());
                            }
                        } else {
                            throw new IllegalArgumentException("Unsupported device: " + deviceName + ". Not found in devices.json.");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        baseContext = browser.newContext(options);
        targetContext = browser.newContext(options);
        basePage = baseContext.newPage();
        targetPage = targetContext.newPage();
    }

    @After("@parity")
    public void teardownParity() {
        if (baseContext != null) baseContext.close();
        if (targetContext != null) targetContext.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Given("I prepare a parity test named {string}")
    public void i_prepare_a_parity_test_named(String name) {
        this.testName = name;
    }

    @When("I load the base URL {string} and target URL {string}")
    public void i_load_urls(String baseUrl, String targetUrl) {
        // Resolve targetUrl via system property if dynamic override is required, fallback to feature file URL
        String envTargetUrl = System.getProperty("target.url");
        String finalTargetUrl = (envTargetUrl != null && !envTargetUrl.isEmpty()) ? envTargetUrl : targetUrl;
        
        String envBaseUrl = System.getProperty("base.url");
        String finalBaseUrl = (envBaseUrl != null && !envBaseUrl.isEmpty()) ? envBaseUrl : baseUrl;

        // Playwright objects are not thread-safe. Must be run synchronously on the main thread.
        basePage.navigate(finalBaseUrl);
        basePage.waitForLoadState(LoadState.NETWORKIDLE);

        targetPage.navigate(finalTargetUrl);
        targetPage.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Then("the DOM structure should be identical")
    public void dom_structure_should_be_identical() {
        DomParityService.compareDom(basePage, targetPage);
    }

    @Then("the visual layout should be identical")
    public void visual_layout_should_be_identical() throws IOException {
        VisualParityService.comparePages(basePage, targetPage, testName);
    }
}
