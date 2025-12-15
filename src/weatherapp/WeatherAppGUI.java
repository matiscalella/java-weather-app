package weatherapp;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.json.simple.JSONObject;
import weatherapp.api.WeatherAPIService;


public class WeatherAppGUI extends JFrame {
    
    private JSONObject weatherData;
    public WeatherAppGUI() {
        super("JAVA Weather App"); // Title
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit program on GUI closing
        setSize(450, 650); // GUI size
        setLocationRelativeTo(null); // Center GUI
        setLayout(null); // Set layout manager null to position components within the gui
        setResizable(false); // Prevent resizing of the GUI
        
        addGuiComponents();
    }

    private void addGuiComponents() {
        
        // Search Field
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Fira Code", Font.PLAIN, 24));
        add(searchTextField);
        
        

        // Weather Image
        JLabel weatherConditionImage = new JLabel(loadImage("/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);
        
        // Temperature
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("JetBrains Mono", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);
        
        // Weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("JetBrains Mono", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);
        
        // Humidity Image
        JLabel humidityImage = new JLabel(loadImage("/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);
        
        // Humidity Text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        add(humidityText);
        
        // Windspeed Image
        JLabel windspeedImage = new JLabel(loadImage("/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);
        
        // Windspeed Text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15 km/h</html>");
        windspeedText.setBounds(310, 500, 85, 55);
        windspeedText.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        add(windspeedText);
        
        // Search Button
        JButton searchButton = new JButton(loadImage("/assets/search.png"));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setContentAreaFilled(false);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Get location from user
                String userInput = searchTextField.getText();
                // Validate and remove whitespace
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }
                // Retrieve weather data
                weatherData = WeatherAPIService.getWeatherData(userInput);
                if (weatherData == null) {
                    JOptionPane.showMessageDialog(
                        WeatherAppGUI.this,
                        "Could not obtain climate for this location",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                // Update GUI
                
                // Update weather Image
                String weatherCondition = (String) weatherData.get("weather_condition");
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("/assets/snow.png"));
                        break;
                }
                // Update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " °C");
                
                // Update weather condition
                weatherConditionDesc.setText(weatherCondition);
                
                // Update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");
                
                // Update windspeed text
                double windspeed = (double) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + " km/h</html>");
                
            }
        });
        add(searchButton);
    }
    
    private ImageIcon loadImage(String resourcePath) {
        java.net.URL imgURL = WeatherAppGUI.class.getResource(resourcePath);

        if (imgURL == null) {
            System.err.println("No image found: " + resourcePath);
            return null;
        }

        return new ImageIcon(imgURL);
    }
}
