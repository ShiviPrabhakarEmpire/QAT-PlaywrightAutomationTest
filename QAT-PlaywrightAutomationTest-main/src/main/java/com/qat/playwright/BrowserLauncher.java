package com.qat.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BrowserLauncher {
    private static final String TARGET_URL = "https://www.google.com";

    private BrowserLauncher() {
    }

    public static void main(String[] args) {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean keepOpen = Boolean.parseBoolean(System.getProperty("keepOpen", Boolean.toString(!headless)));
        List<String> requestedBrowsers = parseBrowsers(System.getProperty("browsers", "chromium,firefox,webkit"));
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);

        try (Playwright playwright = Playwright.create()) {
            List<Browser> browsers = new ArrayList<>();
            try {
                for (String browserName : requestedBrowsers) {
                    Browser browser = launch(playwright, browserName, headless);
                    browsers.add(browser);
                    Page page = browser.newPage();
                    page.navigate(TARGET_URL);
                    System.out.printf("Opened %s at %s%n", browserName, page.url());
                }

                if (keepOpen) {
                    System.out.println("Browsers are open. Press Enter to close them.");
                    System.in.read();
                }
            } finally {
                for (Browser browser : browsers) {
                    browser.close();
                }
            }
        } catch (Exception exception) {
            System.err.println("Unable to launch Playwright browsers: " + exception.getMessage());
            System.err.println("Run './gradlew installBrowsers' if the browser binaries are not installed.");
            System.exit(1);
        }
    }

    private static List<String> parseBrowsers(String value) {
        List<String> browsers = List.of(value.split(","));
        if (browsers.stream().anyMatch(browser -> !List.of("chromium", "firefox", "webkit").contains(browser.trim().toLowerCase(Locale.ROOT)))) {
            throw new IllegalArgumentException("browsers must contain only chromium, firefox, or webkit");
        }
        return browsers.stream().map(browser -> browser.trim().toLowerCase(Locale.ROOT)).toList();
    }

    public static Browser launch(Playwright playwright, String browserName, boolean headless) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
        return browserType(playwright, browserName).launch(launchOptions);
    }

    public static BrowserType browserType(Playwright playwright, String browserName) {
        return switch (browserName) {
            case "chromium" -> playwright.chromium();
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };
    }
}