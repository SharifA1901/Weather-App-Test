Weather App - Spring Boot
This is a simple Spring Boot application that provides weather-related features using the Visual Crossing Weather API.

Features
✅ Fetches weather forecast data for a given city.
✅ Compares daylight hours between two cities.
✅ Checks if it is currently raining in either of two cities.
✅ Implements exception handling to ensure stability.
✅ Includes unit tests to verify correctness.

Endpoints

GET /weather/forecast/{city} → Returns weather forecast for the city.
GET /weather/daylight/{city1}/{city2} → Compares daylight hours between two cities.
GET /weather/raincheck/{city1}/{city2} → Checks for rain in the given cities.

Setup & Installation
Clone this repository:
git clone https://github.com/yourusername/myweatherapp.git
Navigate into the project folder:
cd myweatherapp
Configure API Key in src/main/resources/application.properties:
weather.visualcrossing.key=YOUR_API_KEY
Build and run the application:
mvn spring-boot:run
Run tests:
mvn test

