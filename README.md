# QAT Playwright Automation - Empire Life

A robust functional test automation framework for Empire Life built with **Java**, **Playwright**, **Cucumber (BDD)**, and **Gradle**.

## Overview
This repository contains automated functional tests targeting `empire.ca`. The tests are structured professionally by domain and feature boundaries, ensuring comprehensive test coverage across multiple browsers and devices.

### Domain Features Covered
*   **Homepage Navigation:** Tests the mega-menu navigation across all devices.
*   **Global Search:** Verifies that search functionality correctly resolves queries.
*   **Login Portals:** Ensures dropdown log-in links redirect users to correct portals (e.g., MyEmpire).

## Cross-Browser, Cross-Device, and Locales

The test suite is designed to dynamically emulate various environments directly using System properties.

Currently supported and tested configurations include:
- **Browsers:** `chromium`, `firefox`, `webkit`
- **Devices:** `Desktop` (standard viewport), `iPhone 13`, `Pixel 5`
- **Locales:** English (`@EN`) and French (`@FR`)

This ensures that critical site components (like the responsive mobile hamburger menu and desktop mega-menus) are validated against multiple combinations of rendering engines, screen sizes, and translations.

## Requirements

- Java 21 or newer
- Gradle wrapper included in this repository

## Quick Start

### 1. Install browser binaries (Optional - Automated in script)

Playwright requires specific browser binaries to run. The wrapper script handles this automatically, but you can also install them manually:

```bash
./gradlew installBrowsers
```

### 2. Run the Test Suite

We've provided a simple `run-tests.sh` script to make this repository truly **"clone-and-run"**. It automatically resolves your Java 21 `JAVA_HOME`, installs missing OS/browser dependencies, and forwards arguments to Gradle.

```bash
# Run all tests on Chrome Desktop (Default)
./run-tests.sh

# Run tests on a specific mobile device (e.g., Pixel 5)
./run-tests.sh -Dbrowser=chromium -Ddevice=Pixel_5

# Run only English tests on Desktop Safari (WebKit)
./run-tests.sh -Dbrowser=webkit -Ddevice=Desktop -Dcucumber.filter.tags="@EN"

# Run only French tests on Mobile iPhone 13
./run-tests.sh -Dbrowser=webkit -Ddevice=iPhone_13 -Dcucumber.filter.tags="@FR"
```

## Sample Execution Report

When running the test suite via Gradle, you will see a console execution report detailing the Cucumber steps executed for the given environment:

```console
> Task :test
Running tests with Browser: chromium, Device: Pixel_5

Empire Life Homepage Navigation
  Verify main navigation links
    passed
    passed
    passed

Empire Life Search Functionality
  Verify search works correctly
    passed

Empire Life Mega Menu Sub-links
  Navigate to Term Life Insurance from menu
    passed
  Navigate to Annuities from menu
    passed

Empire Life Login Portals
  Customer navigates to MyEmpire portal
    passed

BUILD SUCCESSFUL in 14s
```

## Advanced Execution Options

The core runner allows launching headed browsers manually. 
To run headlessly and close browsers after navigation:

```bash
./gradlew run -Dheadless=true -DkeepOpen=false
```

Open only selected browsers:

```bash
./gradlew run -Dbrowsers=chromium,firefox
```
