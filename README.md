# Weather App

## Description

The Weather App is a web application that fetches and displays weather data for various locations using the OpenWeatherMap API.
Users can search for current weather conditions and receive detailed information such as temperature, humidity, wind speed, and weather descriptions.

## Features

- **Search for Weather Information**: Enter the name of a city to retrieve weather information.
- **Display Weather Data**: Show current weather conditions including temperature, humidity, wind speed, sunrise, sunset and weather descriptions.
- **Error Handling**: Display user-friendly error messages if the city is not found or if there is an issue with the API.
- **Weather Forecast**: Display a 3-hour/30-hour weather forecast for the selected city.
- **Geolocation**: Automatically detect the user’s location using Javascript's geolocation and display the weather using coordinates.
- **GUI Enhancements**: Use HTML, CSS, Javascript and Thymeleaf for an improved frontend experience.
- **User Preferences**: Let users choose between metric and imperial units.
- **Rate Limiting**: Ensure fair usage by limiting the number of API requests per user.
- **Caching**: Improve performance and reduce API calls using in-memory caching.

## Technologies Used

### Frontend (GUI)

- **HTML**: Structure the content of the web pages.
- **CSS**: Style and design the web pages for a better visual experience.
- **Javascript**: Enable geolocation and smooth page transitions.
- **Thymeleaf**: A Java template engine that integrates with Spring Boot to create dynamic web pages.

### Backend

- **Spring Boot**: Framework used for building the backend of the application.
- **Spring MVC**: Framework for handling web requests and defining controllers.
- **Spring Data JPA/PostgreSQL (Coming Soon)**: (Coming Soon) Database for storing user preferences or historical data.
- **RestTemplate**: For making RESTful API calls to the OpenWeatherMap API.
- **JUnit**: Testing framework for writing and running unit tests.
- **Caffeine Cache**: Used for in-memory caching of weather data to improve performance.
- **Bucket4j**: Library used for implementing rate limiting to control API requests.
- **Mockito**: Framework for creating mock objects and writing unit tests.
- **OpenWeatherMap API**: Public API used for fetching weather data.

## Installation

1. **Clone the repository**:
   `git clone https://github.com/Aemyrre/weather-app.git`
2. **Navigate to the project directory**:
   `cd your-uri-path`
3. **Install dependencies**:
   `./mvnw install`
4. **Obtain an API key from OpenWeatherMap and add it to the application.properties file**:
   `weather.api.key=YOUR_API_KEY`
5. **Run the application**:
   `./mvnw spring-boot:run`

## Usage

Open a web browser and navigate to http://localhost:8080.

Enter the name of a city in the search box to retrieve weather information.

## Screenshots

### API

1. Using single argument (i.e., name of place)
<div align="center" width="229" height="431.5">
<!-- ![image](https://github.com/user-attachments/assets/6278ef9e-a293-4e56-8ced-cf5a61dcba37) -->
   <img src="https://github.com/user-attachments/assets/6278ef9e-a293-4e56-8ced-cf5a61dcba37" alt="RESTful API Weather app screenshot-1" />
</div>

2. Using multiple arguments (i.e., city, unit of measurement, and language)
<div align="center">
<!-- ![image](https://github.com/user-attachments/assets/381fff3f-bb3f-4214-b75b-5f0039fc2d7d) -->
   <img src="https://github.com/user-attachments/assets/381fff3f-bb3f-4214-b75b-5f0039fc2d7d" alt="RESTful API Weather app screenshot-2"/>
</div>

### Server-Side Rendering

1. Using `ssr` branch and `http://localhost:8080`
<div align="center">
<!-- ![Screenshot 2025-04-19 195143](https://github.com/user-attachments/assets/85dda674-9c55-4515-b6bf-00d2c669a111) -->
   <img src="https://github.com/user-attachments/assets/85dda674-9c55-4515-b6bf-00d2c669a111" alt="Weather API frontend screenshot"/>
</div>

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

## Contact

For any inquiries or feedback, please drop me a message.
