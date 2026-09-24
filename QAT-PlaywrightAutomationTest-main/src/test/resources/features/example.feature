Feature: Example feature

  Scenario Outline: Open search engine in browser
    Given I launch the "<browser>" browser
    When I navigate to "https://www.google.com"
    Then the page title should contain "Google"

    Examples:
      | browser  |
      | chromium |
      | firefox  |
      | webkit   |
