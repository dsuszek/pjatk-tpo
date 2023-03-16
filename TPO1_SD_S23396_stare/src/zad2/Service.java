package zad2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class Service {

    private String country;
    private CurrencyCodes currencyCodes = new CurrencyCodes();
    private Connection connection = new Connection();


    // Constructor with one parameter - name of country
    Service(String country) {
        this.country = country;
    }

    String getWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=0d1a33b85a66b2ec8437ebd21d77afc9";

        try {
            return Connection.makeRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double getRateFor(String currencyCode) {
        String baseCurrencyCode = CurrencyCodes.getCurrencyCode(this.country);

        String url = String.format("https://api.exchangerate.host/convert?from=%s&to=%s", currencyCode, baseCurrencyCode);
        String data;

        try {
            return Double.valueOf(Connection.makeRequest(url));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Double getNBPRate(String table) {
        String currencyCode = CurrencyCodes.getCurrencyCode(this.country);

        // When we compare PLN to PLN, rate will always be 1.0
        if (currencyCode.equals("PLN")) {
            return 1d;
        }

        String tableUrl = String.format("http://www.nbp.pl/kursy/kursya.html", table);
        String tablePageData;

        try {
            return Double.valueOf((Connection.makeRequest(tableUrl)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
