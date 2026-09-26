Feature: Empire Life Homepage Navigation
  As a prospective customer
  I want to navigate through the main menu
  So that I can find information on Insurance, Investments, and Group Benefits

  @EN
  Scenario Outline: Verify main navigation links
    Given I launch the browser
    And I navigate to the Empire page "https://www.empire.ca/"
    When I click on the "<menu_item>" link
    Then I should be redirected to a page containing "<expected_url_path>"

    Examples: 
      | menu_item      | expected_url_path |
      | Insurance      | /insurance        |
      | Investments    | /investments      |
      | Group Benefits | /group-benefits   |

  @FR
  Scenario Outline: Verify main navigation links in French
    Given I launch the browser
    And I navigate to the Empire page "https://www.empire.ca/fr"
    When I click on the "<menu_item>" link
    Then I should be redirected to a page containing "<expected_url_path>"

    Examples: 
      | menu_item            | expected_url_path |
      | Assurance            | /insurance        |
      | Placements           | /investments      |
      | Assurance collective | /group-benefits   |
