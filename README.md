# QAT Playwright Automation

Java Playwright automation framework managed with Gradle. The sample launcher opens Google in Chromium, Firefox, and WebKit.

## Requirements

- Java 21 or newer
- Gradle wrapper included in this repository

## Install browser binaries

```bash
./gradlew installBrowsers
```

## Open Google in all browsers

The default is headed mode and keeps the browser windows open until you press Enter:

```bash
./gradlew run
```

## Useful options

Run headlessly and close browsers after navigation:

```bash
./gradlew run -Dheadless=true -DkeepOpen=false
```

Open only selected browsers:

```bash
./gradlew run -Dbrowsers=chromium,firefox
```

Supported browser names are `chromium`, `firefox`, and `webkit`.

## Run tests

To execute the automated tests, use the following Gradle command:

```bash
./gradlew test
```
