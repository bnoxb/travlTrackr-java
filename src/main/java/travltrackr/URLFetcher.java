package travltrackr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// This things purpose was to test out a sloppier way of making the api call
public class URLFetcher {
    public static Object urlFetch() throws Exception {
        URL myURL = new URL("https://api.darksky.net/forecast/f731e2c5a8a857fbe7119a0c1b5e76c9/39.693967,-104.911074?exclude=minutely,hourly");
        HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder results = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            results.append(line);
        }

        connection.disconnect();
        System.out.println(results.toString());
        return results;
    }
}
