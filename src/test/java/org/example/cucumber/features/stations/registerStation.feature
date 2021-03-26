Feature: Register weather station
  As a customer, I want to register new weather station using the OpenWeatherMap service

  @positive
  @smoke
  @all
  Scenario Outline: Register valid station with active customer API key
    Given a customer has active API key <appid>
    When a customer provides valid station info
      |external_id  |name  | longitude  |latitude   |altitude   |
      |<external_id>|<name>|<longitude> |<latitude> |<altitude> |
    Then the service gives response status code 201

    Examples:
    |appid                              |external_id |name           |longitude |latitude |altitude |
    |"643f0696190d59412564908853c0bfc6" |MSK_TEST001 |Minsk name 001 |27.57     |-53.9    |222      |


  #-----------------------------------------------------------------------------------------------------------
  # TODO: add userID, source type validation
  @positive
  @all
  Scenario Outline: Register valid station with active customer API key and boundary param values
    Given a customer has active API key <appid>
    When a customer provides valid station info
      |external_id  |name  | longitude  |latitude   |altitude   |
      |<external_id>|<name>|<longitude> |<latitude> |<altitude> |
    Then the service gives response status code 201
    And response includes valid station info, station ID and creation time
      |external_id  |name  | longitude  |latitude   |altitude   |
      |<external_id>|<name>|<longitude> |<latitude> |<altitude> |

    Examples:
      |appid                              |external_id |name           |longitude |latitude |altitude |
      |"04be6dc4d113ef593f214faf71db2d86" |3           |3              |-179.99999990|-89.94567868000|-3000000.000|
      |"04be6dc4d113ef593f214faf71db2d86" |4           |4              |179.9999999999999|89.99999999999999|99999.99999999999|
      |"04be6dc4d113ef593f214faf71db2d86" |5           |5              |-179.9999999999999|-89.99999999999999|-99999.99999999999|
    # the next name and external_id params are definitely not ok. Bug?
      |"643f0696190d59412564908853c0bfc6" |!@#$%^&*()_+\|/'"<>? |!@#$%^&*()_+\|/'"<>? |-27.57 |53.9 |222.1 |


  #-----------------------------------------------------------------------------------------------------------
  @positive
  @all
  Scenario Outline: Register station with empty position params
    Given a customer has active API key "643f0696190d59412564908853c0bfc6"
    When a customer provides station info with empty position params
      |external_id  |name  | longitude  |latitude   |altitude   |
      |1            |1     |<longitude> |<latitude> |<altitude> |
    Then the service gives response status code 201

    Examples:
      |longitude |latitude |altitude |
      |          |4        |5        |
      |3         |         |5        |
      |3         |4        |         |
      |          |         |         |


  #-----------------------------------------------------------------------------------------------------------
  @negative
  @all
  Scenario: Register station without station info
    Given a customer has active API key "643f0696190d59412564908853c0bfc6"
    When a customer does not provide station info
    Then the service gives response status code 400
    And response message includes "EOF"


  #-----------------------------------------------------------------------------------------------------------
  @negative
  @all
  Scenario: Register station with wrong station info
    Given a customer has active API key "643f0696190d59412564908853c0bfc6"
    When a customer provides wrong station info
    |weather|cold |
    |date   |today|
    Then the service gives response status code 400


  #-----------------------------------------------------------------------------------------------------------
  @negative
  @all
  Scenario: Register valid station without customer API key
    Given a customer has not API key
    When a customer provides valid station info
      |external_id |name           | longitude |latitude |altitude |
      |MSK_TEST001 |Minsk name 001 |27.57      |53.9     |222      |
    Then the service gives response status code 401
    And response message includes "Invalid API key."


  #-----------------------------------------------------------------------------------------------------------
  @negative
  @all
  Scenario Outline: Register valid station with inactive customer API key
    Given a customer has inactive API key <appid>
    When a customer provides valid station info
      |external_id |name           | longitude |latitude |altitude |
      |MSK_TEST001 |Minsk name 001 |27.57      |53.9     |222      |
    Then the service gives response status code 401
    And response message includes "Invalid API key."

    Examples:
      |appid      |
      |"1234abcd" |
      |""         |


  #-----------------------------------------------------------------------------------------------------------
  @negative
  @all
  Scenario Outline: Register station with incorrect longitude or latitude
    Given a customer has active API key "643f0696190d59412564908853c0bfc6"
    When a customer provides station info with incorrect longitude or latitude
      |external_id  |name           |longitude  |latitude   |altitude   |
      |MSK_TEST001  |Minsk name 002 |<longitude>|<latitude> |222        |
    Then the service gives response status code 400
    And response message includes <message>

    Examples:
      |longitude        |latitude         |message                                    |
      |181              |45               |"Station longitude should be in (-180:180)"|
      |180.0000000000001|12               |"Station longitude should be in (-180:180)"|
      |-12345678910     |5                |"Station longitude should be in (-180:180)"|
      |33               |91               |"Station latitude should be in (-90:90)"   |
      |-10              |90.00000000000001|"Station latitude should be in (-90:90)"   |
      |36               |-12345678910     |"Station latitude should be in (-90:90)"   |
      |12345678910      |12345678910      |"Station latitude should be in (-90:90)"   |


  #-----------------------------------------------------------------------------------------------------------
  @negative
  @all
  Scenario Outline: Register station with empty station name or external_id
    Given a customer has active API key "643f0696190d59412564908853c0bfc6"
    When a customer provides empty station name or external_id
      |external_id  |name  | longitude  |latitude   |altitude   |
      |<external_id>|<name>|3           |4          |5          |
    Then the service gives response status code 400
    And response message includes <message>

    Examples:
    |external_id |name    |message                           |
    |            |2       |"Bad external id"                 |
    |            |        |"Bad external id"                 |
    |1           |        |"Bad or zero length station name" |
