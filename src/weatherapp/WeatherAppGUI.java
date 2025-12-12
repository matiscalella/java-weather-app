package weatherapp;

import java.awt.Cursor;
import java.awt.Font;
import javax.swing.*;


public class WeatherAppGUI extends JFrame {
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
        
        // Search Button
        JButton searchButton = new JButton(loadImage("/assets/search.png"));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setContentAreaFilled(false);
        add(searchButton);

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
