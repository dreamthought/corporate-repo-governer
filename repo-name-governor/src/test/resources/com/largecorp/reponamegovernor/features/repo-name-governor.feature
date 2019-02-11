Feature: Validating repositories are named in line with Governance (Tm)

  As a technical platform owner I want new repositories to follow our conventions
  So that developers across the organisation have a shared taxonomy and understanding

  Scenario Outline: A user creates a microservice in an approved language with a well named repository
    Given a user needs a new <APP_TYPE>
    When  a repository is created named <APP_TYPE>-<TECH>-<DOMAIN>-application
    Then an issue in github will not be created
    Examples:
      | APP_TYPE | TECH | DOMAIN |
      | microservice    | springboot | finance     |
      | frontend        | angular    | research    |
      | lambda          | python     | datascience |
      | monolith        | PHP        | sales       |
      | webapp          | RoR        | product     |


