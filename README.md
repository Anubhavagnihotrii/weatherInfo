# WeatherInfo Application

## Overview
The **WeatherInfo** application provides weather details based on a given pincode. It uses OpenWeatherMap API to retrieve geolocation and weather information. The data is stored in a MySQL database for caching purposes and is returned for subsequent requests if the same data exists.

## Features
- **Fetch Weather Info**: Get current weather details based on pincode and date.
- **Store Weather Info**: Save weather data in the database for efficient future retrieval.
- **Geolocation Integration**: Retrieve latitude and longitude using the pincode from OpenWeatherMap API.
- **Weather Data**: Fetch weather details (temperature, humidity, wind speed, etc.) using OpenWeatherMap API.

## API Documentation
The API documentation for this project is available on Postman:  
https://documenter.getpostman.com/view/39581574/2sAY52dfG4

## Prerequisites
- **JDK 22** or higher should be installed.
- **Maven** (Optional) if using intellij not required
- **MySQL** (for storing weather data)
- **Postman** (for testing the API)

## Project Setup

### 1. Clone the Repository
First, clone the repository to your local machine:
```bash
git clone <repository-url>
cd <project-directory>
```

### 2. Set up the Database

1. **Install MySQL**: Make sure MySQL is installed and running on your local machine or use a remote MySQL instance.
2. **Create Database**: The application will automatically create the `weatherapp` database if it doesn't already exist. If you wish to manually create the database, you can execute:
    ```sql
    CREATE DATABASE weatherapp;
    ```

3. **Database Configuration**: In `src/main/resources/application.properties`, update the following properties to match your MySQL configuration:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/weatherapp?createDatabaseIfNotExist=true&useSSL=false&useUnicode=true&characterEncoding=utf-8&autoReconnect=true
   spring.datasource.username=YOUR_MYSQL_USERNAME
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.generate-ddl=true
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.defer-datasource-initialization=false
   spring.jpa.show-sql=true
   ```

   Replace the username, password, and database URL as per your local MySQL configuration.

4. **Weather API Key**: In the `application.properties` file, you must provide your OpenWeatherMap API key: <br/>
     go to https://openweathermap.org/current and generate your api-key
   ```properties
   weather.api.key=YOUR_WEATHER_API_KEY
   ```
    add above to your application.properties, and put you api-key

### 3. Build and Run the Application
Go to ```src/main/java/com/example/WeatherInfo/WeatherInfoApplication.java``` file and run the app.
### 4. Test the API

You can use **Postman** to test the API requests. Import the provided collection link into Postman:
https://documenter.getpostman.com/view/39581574/2sAY52dfG4

