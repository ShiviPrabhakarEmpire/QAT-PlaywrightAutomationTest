package com.qat.playwright.steps;

import com.microsoft.playwright.Browser;
import com.qat.playwright.BrowserLauncher;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class CommonSteps {

    private final TestContext testContext;

    public CommonSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("I launch the browser")
    public void i_launch_the_browser() {
        String browserName = com.qat.playwright.utils.ConfigReader.getProperty("BROWSER", "chromium");
        String deviceName = com.qat.playwright.utils.ConfigReader.getProperty("DEVICE", "Desktop");
        boolean headless = Boolean.parseBoolean(com.qat.playwright.utils.ConfigReader.getProperty("HEADLESS", "true"));
        
        testContext.browser = BrowserLauncher.launch(testContext.playwright, browserName, headless);
        
        if ("Desktop".equalsIgnoreCase(deviceName)) {
            Browser.NewContextOptions desktopOptions = new Browser.NewContextOptions();
            desktopOptions.setViewportSize(1920, 1080);
            testContext.context = testContext.browser.newContext(desktopOptions);
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
            testContext.context = testContext.browser.newContext(deviceOptions);
        }
        testContext.page = testContext.context.newPage();
    }

    @When("I navigate to {string}")
    public void i_navigate_to(String url) {
        String finalUrl = url.replace("https://www.empire.ca", com.qat.playwright.utils.ConfigReader.getProperty("BASE_URL", "https://www.empire.ca"));
        testContext.page.navigate(finalUrl);
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedTitle) {
        Assertions.assertTrue(testContext.page.title().contains(expectedTitle));
    }
    
    @Then("the page layout should match the baseline {string}")
    public void the_page_layout_should_match_the_baseline(String baselineName) {
        testContext.page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        try {
            java.nio.file.Path screenshotsDir = java.nio.file.Paths.get("build/screenshots");
            if (!java.nio.file.Files.exists(screenshotsDir)) {
                java.nio.file.Files.createDirectories(screenshotsDir);
            }
            testContext.page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
                    .setPath(screenshotsDir.resolve(baselineName + ".png")));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Failed to take screenshot: " + e.getMessage());
        }
    }
}
