
package weatherapp.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* This class retrieves weather from API and returns it
    GUI will display this data to the user
*/
public class WeatherAPIService {
    // Fetch weather data for given location
    public static JSONObject getWeatherData(String locationName) {
            // Get location coordenates
            JSONArray locationData = getLocationData(locationName);
            
            // Extract latitude and longitude data
            JSONObject location = (JSONObject) locationData.get(0);
            double latitude = (double) location.get("latitude");
            double longitude = (double) location.get("longitude");
            
            // Build API request URL with location coordinates
            String urlString = "https://api.open-meteo.com/v1/forecast?"
        + "latitude=" + latitude
        + "&longitude=" + longitude
        + "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m";
            try {
                // Call API and get response
                HttpURLConnection conn = fetchApiResponse(urlString);
                // Check response status
                if (conn.getResponseCode()!= 200) {
                    System.out.println("Error: Could not connect to API");
                    return null;
                }
                // Store result JSON
                StringBuilder resultJson = new StringBuilder();
                
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    // Read and store in the string builder
                    resultJson.append(scanner.nextLine());
                }
                
                scanner.close();
                conn.disconnect();
                
                // Parse though data
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
                
                // retieve hourly Data
                JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
                JSONArray time = (JSONArray) hourly.get("time");
                int index = findIndexOfCurrentTime(time);
                
                // get temperature
                JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
                double temperature = (double) temperatureData.get(index);
                
                // get weather code
                JSONArray weathercode = (JSONArray) hourly.get("weathercode");
                String weatherCondition = convertWeatherCode((long) weathercode.get(index));
                
                // Humidity data
                JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
                long humidity = (long) relativeHumidity.get(index);
                
                // Windspeed data
                JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
                double windspeed = (double) windspeedData.get(index);
                
                // build the weather json data for GUI
                JSONObject weatherData = new JSONObject();
                weatherData.put("temperature", temperature);
                weatherData.put("weather_condition", weatherCondition);
                weatherData.put("humidity", humidity);
                weatherData.put("windspeed", windspeed);
                
                return weatherData;
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return null;
        } 

    public static JSONArray getLocationData(String locationName) {
        // Replace whitespace with "+" character to match API's request format
        locationName = locationName.replaceAll(" ", "+");
        // Builds API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="+ locationName + "&count=10&language=en&format=json";
        
        try {
            // Call API
            HttpURLConnection conn = fetchApiResponse(urlString);
            
            // Check response status (200 is ok)
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: could not connect to API.");
                return null;
            } else {
                // Store API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                
                // Read and store resulting json data into StringBuilder
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                // Close scanner
                scanner.close();
                // Close url connection
                conn.disconnect();
                
                // Parse the json string into an object
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
                
                // Get the list of location data generated by the API
                JSONArray locationData = (JSONArray) resultJsonObj.get("results");
                return locationData;
            }         
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    private static HttpURLConnection fetchApiResponse(String urlString){
        try {
            // Attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Set request method
            conn.setRequestMethod("GET");
            //Connect to the API
            conn.connect();
            return conn;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();
        // Iterate through the time list and find the match for current time
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                // Returns index match
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime() {
        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Format date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        
        String formattedDateTime = currentDateTime.format(formatter);
        
        return formattedDateTime;
    }

    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";
        if (weathercode == 0L) {
            weatherCondition = "Clear";
        } else if (weathercode <= 3L && weathercode > 0) {
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L)
            || (weathercode >= 80L && weathercode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            weatherCondition = "Snow";
        }   
        return weatherCondition;
    }
}
