import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.*;

public class VulnerableApp {

    private static final String DB_URL = "jdbc:mysql://mydatabase.com/mydb";
    private static final String DB_USER = System.getEnv("SQL_USER");
    private static final String DB_PASSWORD = System.getEnv("SECRET");

    private static final Logger logger = Logger.getLogger(VulnerableApp.class.getName());

    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }

    public static void sendEmail(String to, String subject, String body) {
        try {
            String command = String.format("echo %s | mail -s \"%s\" %s", body, subject, to);
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            logger.log(Level.ERROR, "Email Error", e);
        }
    }

    public static String getData() {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("https://secure-api.com/get-data");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
        } catch (Exception e) {
            logger.log(Level.ERROR, "Error fetching data", e);
        }

        return result.toString();
    }

    public static void saveToDb(String data) {
        String query = "INSERT INTO mytable (column1, column2) VALUES ('" + data + "', 'Another Value')";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(query);
            System.out.println("Data saved to database.");

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database error", e);
        }
    }

    public static void main(String[] args) {
        String userInput = getUserInput();
        String data = getData();
        saveToDb(data);
        sendEmail("admin@example.com", "User Input", userInput);
    }

}


