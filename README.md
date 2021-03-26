# Weathermap api testing

This project was created to show my ability to use different AT tools and approaches in REST API testing. 
I have chosen openweathermap.org service with their free and well-documented easy-to-work weather API. 
And then decided to test API for managing personal weather stations. My project can test registration, change and station remove.
I know that I have not developed all possible test scenarios. But even that amount of scenarios helped me to **FIND SEVERAL BUGS** there:
1. I find out that openweathermap service allows save stations with special characters (<>/'~ and so on) in station names. As known such behaviour could lead to cross site scripting. 
2. The service lets register stations with altitude parameter more than -12 742 000 meters. So depth could be more than earth diameter!
3. Response body for register and change requests has different names of id fields (ID and id). It is much better to name them in one manner because all other JSON-object fields have identical names.


## I used following tools in my project:

* **Cucumber** - to write and run acceptance tests in easily readable and understandable format (based on BDD approach)
* **PicoContainer** - to share state between different classes with scenario step definitions (Dependency injection framework)
* **Rest-assured** - to create customizable HTTP Requests for sending to the server and validate the HTTP Responses received from it
* **Junit** - to organize tests and make assertions
* **Hamcrest** - to match actual and expected result in scenario assertions
* **FasterXML/jackson** - to serialize POJO to JSON and deserialize back
* **Maven** - to download the project dependency libraries and run project


## How to run project:

Open up a terminal and move to the project folder on your local machine.
Run the maven test command:
`mvn -Dtest=CucumberRunner test`



