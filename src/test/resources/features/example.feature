Feature: Advisor Portal features

  @login
  Scenario Outline: Advisor Portal Login
    Given I launch the "<browser>" browser
    When I navigate to "https://portal.mypl.empire.ca/"
    Then the page title should contain "Log in | Empire Life"
    Then Add <username> and <password>
    Then Home page should contain "Home"
    Examples:
      | browser  | username    | password  |
      | chromium |  754250Test | Empire100 |
