package travltrackr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class HttpRequest {


    public final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    String URLSTRING = "https://api.yelp.com/v3/businesses/search?location=Denver";
    String URLPROTOCOL = "https";
    String URLHOST = "api.yelp.com";
    String URLPATH = "/v3/businesses/search";
    String URLCOMPOUND = URLPROTOCOL + "://" + URLHOST + URLPATH + "?";

    URL url;
    URLConnection urlConnection = null;
    HttpURLConnection connection = null;
    BufferedReader in = null;
    String urlContent = "";

    public String testURL(String urlString, String yelpApiKey, String location, String finalAttr) throws IOException, IllegalArgumentException {
        String urlStringCont = "";
        // comment the if clause if experiment with URL
        String locationQuery = "location=" + location;
        String fullURLString = urlString + locationQuery + finalAttr;

        // creating URL object
        url = new URL(fullURLString);
        // get URL connection
        urlConnection = url.openConnection();
        connection = null;
        String bearerAuth = "Bearer " + yelpApiKey;
        // we can check, if connection is proper type
        if (urlConnection instanceof HttpURLConnection) {
            connection = (HttpURLConnection) urlConnection;
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", bearerAuth);
            connection.setRequestProperty("Content-Type", "application/json");
        } else {
            log.info("Please enter an HTTP URL");
            throw new IOException("HTTP URL is not correct");
        }
        // we can check response code (200 OK is expected)
        log.info(connection.getResponseCode() + " " + connection.getResponseMessage());
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String current;

        while ((current = in.readLine()) != null) {
            urlStringCont += current;
        }
        return urlStringCont;
    }
}

