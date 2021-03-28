Feature: Delete station
  As a customer, I want to delete a station using the OpenWeatherMap service

  Rule: Remove exist station

    Background:
      Given there is registered weather station for "643f0696190d59412564908853c0bfc6" API key
        |external_id  |name        |longitude  |latitude |altitude |
        |st_change_id |st_change_n |1          |2        |3        |

    @smoke
    @positive
    @all
    Scenario: Delete station with active customer API key
      Given a customer with active API key "643f0696190d59412564908853c0bfc6" has chosen the station
      When a customer performs delete request
      Then the service gives response status code 204


    #-----------------------------------------------------------------------------------------------------------
    @negative
    @all
    Scenario: Delete station without active customer API key
      Given a customer without API key has chosen the station
      When a customer performs delete request
      Then the service gives response status code 401
      And response message includes "Invalid API key."


    #-----------------------------------------------------------------------------------------------------------
    @negative
    @all
    Scenario Outline: Delete station with inactive customer API key
      Given a customer with inactive API key <appid> has chosen the station
      When a customer performs delete request
      Then the service gives response status code 401
      And response message includes "Invalid API key."

      Examples:
        |appid      |
        |"1234abcd" |
        |""         |


  Rule: Remove nonexistent station

    @negative
    @all
    Scenario: Delete nonexistent station
      Given a customer with active API key has chosen nonexistent station
        |appid     |643f0696190d59412564908853c0bfc6|
        |stationId |nonexistentstationid            |
      When a customer performs delete request
      Then the service gives response status code 400
      And response message includes "Station id not valid"


    #-----------------------------------------------------------------------------------------------------------
    @negative
    @all
    Scenario: Delete station without choosing it
      Given a customer with active API key "643f0696190d59412564908853c0bfc6" has not chosen the station
      When a customer performs delete request on STATIONS endpoint
      Then the service gives response status code 404
      And response message includes "Internal error"
