package Zadanie_02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Service {

    private String city;

    // Default constructor
    Service() {
    }

    // Constructor with one parameter - name of country
    Service(String country) {
        this.country = country;
    }


    String getWeather(String miasto) {
        InputStream inputStream = null;
        String s = "";

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + miasto + "&appid=0d1a33b85a66b2ec8437ebd21d77afc9");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                String line;
                while ((line = in.readLine()) != null)
                    s += line;
            }
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }


    Double getRateFor(String kod_waluty) throws IOException {
        String url_str = "https://api.exchangerate.host/latest";

        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

//        JsonParser jp = new JsonParser();
//        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
//        JsonObject jsonobj = root.getAsJsonObject();

//        String req_result = jsonobj.get("result").getAsString();
        return null;
    }

    Double getNBPRate() {

        return null;
    }
}
