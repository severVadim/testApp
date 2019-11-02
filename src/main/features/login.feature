@loginFeature
Feature: Test cases to check login functionality

  Background:
    Given host setup

  Scenario Outline: Logging in into account using API.
    When authorize using the URL with userName: <UserName> and password: <Password>
    Then assert that Status is: <StatusCode> and returned JWT token has the same email: <UserName>

    @positive
    Examples:
      | UserName                         | Password | StatusCode |
      | qa.automation.sprinkle@gmail.com | 123456   | 200        |
    @negative
    Examples:
      | UserName                         | Password | StatusCode |
      | qa.automation.sprinkle@gmail.com | 1234567  | 400        |
      | qa.automation.sprinkle@gmail.com | null     | 400        |
      | qa.automation.sprinkle           | 123456   | 400        |
      | automation.sprinkle@gmail.com    | 123456   | 400        |
      | qa.automation.sprinkle@gmail.com | null     | 400        |
      | qa.automation.sprinkle@gmail.com |          | 400        |
      |                                  |          | 400        |

  Scenario Outline: Getting user info.
    When get user info using JWT token from authorization. UserName: <UserName>, Password: <Password>
    Then map response to an object and assert response model is according model

    @positive
    Examples:
      | UserName                         | Password |
      | qa.automation.sprinkle@gmail.com | 123456   |