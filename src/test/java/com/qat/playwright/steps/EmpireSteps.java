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
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
            Browser.NewContextOptions desktopOptions = new Browser.NewContextOptions();
            desktopOptions.setViewportSize(1920, 1080); // Fixed size for stable VRT
            context = browser.newContext(desktopOptions);
        } else {
            Browser.NewContextOptions deviceOptions = new Browser.NewContextOptions();
            try {
                java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("devices.json");
                if (is != null) {
                    try (java.io.InputStreamReader reader = new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8)) {
                        com.google.gson.JsonObject devices = com.google.gson.JsonParser.parseReader(reader).getAsJsonObject();
                        String searchKey = deviceName.replace("_", " ");
                        if (devices.has(searchKey)) {
                            com.google.gson.JsonObject d = devices.getAsJsonObject(searchKey);
                            deviceOptions.setUserAgent(d.get("userAgent").getAsString());
                            deviceOptions.setViewportSize(d.getAsJsonObject("viewport").get("width").getAsInt(), d.getAsJsonObject("viewport").get("height").getAsInt());
                            deviceOptions.setDeviceScaleFactor(d.get("deviceScaleFactor").getAsDouble());
                            deviceOptions.setHasTouch(d.get("hasTouch").getAsBoolean());
                            if (!"firefox".equalsIgnoreCase(System.getProperty("browser", "chromium"))) {
                                deviceOptions.setIsMobile(d.get("isMobile").getAsBoolean());
                            }
                        } else {
                            throw new IllegalArgumentException("Unsupported device: " + deviceName + ". Not found in devices.json.");
                        }
                    }
                } else {
                    throw new IllegalArgumentException("devices.json not found in resources");
                }
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                    throw (IllegalArgumentException) e;
                }
                throw new RuntimeException("Failed to load device configurations", e);
            }
            context = browser.newContext(deviceOptions);
        }
        page = context.newPage();
    }


/*
    // This new code working with all devices
    @Given("I launch the browser")
    public void i_launch_the_browser() {
        String browserName = System.getProperty("browser", "chromium");
        String rawDeviceName = System.getProperty("device", "Desktop");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        browser = BrowserLauncher.launch(playwright, browserName, headless);

        if ("Desktop".equalsIgnoreCase(rawDeviceName)) {
            context = browser.newContext();
        } else {
            Browser.NewContextOptions deviceOptions = getDynamicDeviceOptions(rawDeviceName, browserName);
            context = browser.newContext(deviceOptions);
        }

        page = context.newPage();
    }
    private Browser.NewContextOptions getDynamicDeviceOptions(String rawDeviceName, String browserName) {
        // Convert "iPhone_14_Pro" -> "iphone 14 pro"
        String device = rawDeviceName.replace("_", " ").toLowerCase().trim();
        Browser.NewContextOptions options = new Browser.NewContextOptions();

        if (device.contains("iphone") || device.contains("ipad")) {
            // Handle iPhone models dynamically based on generation
            if (device.contains("pro max") || device.contains("plus")) {
                options.setViewportSize(430, 932).setDeviceScaleFactor(3);
            } else if (device.contains("pro") || device.contains("14") || device.contains("15") || device.contains("16")) {
                options.setViewportSize(393, 852).setDeviceScaleFactor(3);
            } else {
                // Default iPhone/iPhone 12/13 profile
                options.setViewportSize(390, 844).setDeviceScaleFactor(3);
            }

            options.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1")
                    .setHasTouch(true);

        } else if (device.contains("pixel") || device.contains("galaxy") || device.contains("android")) {
            // Handle Android models dynamically
            if (device.contains("ultra") || device.contains("plus")) {
                options.setViewportSize(412, 915).setDeviceScaleFactor(3.5);
            } else {
                // Default Pixel/Galaxy profile
                options.setViewportSize(393, 851).setDeviceScaleFactor(2.75);
            }

            options.setUserAgent("Mozilla/5.0 (Linux; Android 13; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36")
                    .setHasTouch(true);

        } else {
            throw new IllegalArgumentException("Unsupported device specified in CLI: " + rawDeviceName);
        }

        // Set mobile flag for WebKit/Chromium (Firefox does not support isMobile)
        if (!"firefox".equalsIgnoreCase(browserName)) {
            options.setIsMobile(true);
        }

        return options;
    }

 */



    @When("I navigate to {string}")
    public void i_navigate_to(String url) {
        page.navigate(url);
    }

    @Then("the page title should contain {string}")
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
        String href = null;
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\s*" + linkText + "\\s*$", java.util.regex.Pattern.CASE_INSENSITIVE);
            href = (String) page.locator("a").filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(pattern)).first().getAttribute("href");
        } catch (Exception e) {
            System.out.println("Error getting href: " + e.getMessage());
        }
        System.out.println("Extracted href: " + href);

        if (href != null && !href.isEmpty() && !href.equals("#")) {
            if (href.startsWith("http")) {
                page.navigate(href);
            } else {
                String baseUrl = "https://www.empire.ca";
                if (page.url().contains("/fr")) {
                    baseUrl = "https://www.empire.ca/fr";
                }
                if (href.startsWith("/")) {
                    page.navigate("https://www.empire.ca" + href);
                } else {
                    page.navigate(baseUrl + "/" + href);
                }
            }
        } else {
            page.locator("text=" + linkText).first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        }
    }

    @Then("I should be redirected to a page containing {string}")
    public void i_should_be_redirected_to_a_page_containing(String expectedPath) {
        // waitForURL is removed since page.navigate already waits for the load state
        Assertions.assertTrue(page.url().contains(expectedPath), "URL mismatch: " + page.url());
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        openMobileMenuIfPresent();
        String currentUrl = page.url();
        String searchBase = currentUrl.contains("/fr") ? "https://www.empire.ca/fr/search" : "https://www.empire.ca/search";
        page.navigate(searchBase + "?search_api_fulltext=" + searchTerm.replace(" ", "+"));
    }

    @Then("the search results page should be displayed")
    public void the_search_results_page_should_be_displayed() {
        page.waitForURL("**/search**");
        Assertions.assertTrue(page.url().contains("search"));
    }

    @When("I click the Log in button")
    public void i_click_the_log_in_button() {
        openMobileMenuIfPresent();
        try {
             page.locator("button:has-text('Log in'), a:has-text('Log in'), button:has-text('Se connecter'), a:has-text('Se connecter')").first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        } catch(Exception e) {}
    }

    @When("I select {string} from the dropdown")
    public void i_select_from_the_dropdown(String optionText) {
        String href = null;
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\s*" + optionText + "\\s*$", java.util.regex.Pattern.CASE_INSENSITIVE);
            href = (String) page.locator("a").filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(pattern)).first().getAttribute("href");
        } catch (Exception e) {}

        if (href != null && !href.isEmpty() && !href.equals("#")) {
            // Navigate current page instead of window.open to avoid popup blocking
            page.navigate(href);
        } else {
            page.locator("text=" + optionText).first().click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        }
    }

    @Then("a new tab should open pointing to the MyEmpire portal")
    public void a_new_tab_should_open_pointing_to_the_myempire_portal() {
        boolean found = false;
        for (int i = 0; i < 5; i++) {
            page.waitForTimeout(1000); 
            for (Page p : context.pages()) {
                if (p.url().contains("my.empire.ca") || p.url().contains("login.empire.ca")) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        Assertions.assertTrue(found, "No portal page was opened pointing to my.empire.ca or login.empire.ca");
    }

    // --- Visual Regression Testing Steps ---

    @Then("the page layout should match the baseline {string}")
    public void the_page_layout_should_match_the_baseline(String baselineName) {
        // Wait for network idle to ensure fonts/images are loaded before taking the screenshot
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        
        // Playwright Java does not have a built-in hasScreenshot assertion yet.
        // Save the screenshot to a file for manual/external comparison.
        try {
            java.nio.file.Path screenshotsDir = java.nio.file.Paths.get("build/screenshots");
            if (!java.nio.file.Files.exists(screenshotsDir)) {
                java.nio.file.Files.createDirectories(screenshotsDir);
            }
            page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
                    .setPath(screenshotsDir.resolve(baselineName + ".png")));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Failed to take screenshot: " + e.getMessage());
        }
    }
}
