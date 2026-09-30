package com.qat.playwright.steps;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TestContext {
    public Playwright playwright;
    public Browser browser;
    public BrowserContext context;
    public Page page;
}
