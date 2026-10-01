# QAT Playwright Automation - Empire Life

A robust functional and parity test automation framework for Empire Life built with **Java**, **Playwright**, **Cucumber (BDD)**, and **Gradle**.

## Overview
This repository contains automated tests targeting `empire.ca`. The tests are structured professionally by domain and feature boundaries, ensuring comprehensive functional and parity test coverage across multiple browsers and devices.

### Domain Features Covered
*   **Homepage Navigation:** Tests the mega-menu navigation across all devices.
*   **Global Search:** Verifies that search functionality correctly resolves queries.
*   **Login Portals:** Ensures dropdown log-in links redirect users to correct portals (e.g., MyEmpire).
*   **Visual & DOM Parity Testing:** Validates structural and visual identicality between environments using parallel contexts, `xmlunit`, and `image-comparison`.

## Cross-Browser, Cross-Device, and Locales

The test suite dynamically emulates environments via a centralized `devices.json` registry.

Supported environments out of the box:
- **Browsers:** `chromium`, `firefox`, `webkit`
- **Devices:** `Desktop` (1920x1080), `iPhone 13`, `Pixel 5`, `iPad (gen 7)`, `iPad Pro 11`, `iPhone SE`, `Galaxy S21 Ultra`, `Moto G4`
- **Locales:** English (`@EN`) and French (`@FR`)

## Requirements

- Java 21 or newer
- Gradle wrapper included in this repository

## Quick Start

### 1. Install browser binaries

Playwright requires specific browser binaries to run. The wrapper script handles this automatically, but you can also install them manually:

```bash
./gradlew installBrowsers
```

### 2. Run Functional Test Suite

Run the standard functional tests across combinations:

```bash
# Run all tests on Chrome Desktop (Default)
./gradlew test

# Run tests on a specific mobile device (e.g., Galaxy S21 Ultra)
./gradlew test -Ddevice="Galaxy S21 Ultra"
```

### 3. Run Parity Tests (Visual & Structural DOM)

Parity testing launches the base and target URLs in parallel and aggressively filters volatile selectors to ensure robust comparison.

```bash
# Run Parity Checks on Desktop using default environments
./gradlew parityTest

# Override Base/Target URLs dynamically and run on Mobile
./gradlew parityTest -Ddevice="iPhone SE" -Dbase.url="https://base.empire.ca" -Dtarget.url="https://target.empire.ca"
```

## Execution Reports & Screenshots

The framework automatically outputs robust HTML reports and Visual Diffs.

### Real-World Parity Mismatch Examples

When running the suite, the framework streams progress into the console and will throw `org.opentest4j.AssertionFailedError` specifically when it detects DOM or Visual discrepancies:

**1. Desktop Run Execution:**
```console
> Task :parityTest

CucumberTestRunner > Parity Testing for English Empire Site > Verify structural and visual parity for English Homepage FAILED
    org.opentest4j.AssertionFailedError at Constructor.java:502

CucumberTestRunner > Parity Testing for English Empire Site > Verify structural and visual parity for English Search Page FAILED
    org.opentest4j.AssertionFailedError at Constructor.java:502

CucumberTestRunner > Parity Testing for French Empire Site > Verify structural and visual parity for French Homepage FAILED
    org.opentest4j.AssertionFailedError at Constructor.java:502

CucumberTestRunner > Parity Testing for French Empire Site > Verify structural and visual parity for French Search Page FAILED
    org.opentest4j.AssertionFailedError at Constructor.java:502

4 tests completed, 4 failed
```

**2. Mobile Device Execution (e.g. Galaxy S21 Ultra):**
```console
> Task :parityTest

CucumberTestRunner > Parity Testing for English Empire Site > Verify structural and visual parity for English Homepage FAILED
    org.opentest4j.AssertionFailedError at Constructor.java:502

CucumberTestRunner > Parity Testing for English Empire Site > Verify structural and visual parity for English Search Page FAILED
    org.opentest4j.AssertionFailedError at Constructor.java:502
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
