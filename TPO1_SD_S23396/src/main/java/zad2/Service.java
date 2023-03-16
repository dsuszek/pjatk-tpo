package zad2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service {

    private Map<String, String> mapOfCountries = new HashMap<>();
    private static Map<String, Currency> mapOfCurrencies = new HashMap<>();
    private String country;

    // Constructor with one parameter - name of country
    public Service(String country) {
        this.country = country;
    }

    public static String makeRequest(String UrlAPI) throws IOException {

        String s = "";

        try {
            URL url = new URL(UrlAPI);

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



    public static String getCurrencyCode(String country) {
        for (Locale locale : Locale.getAvailableLocales()) {
            String country_key = locale.getCountry();
            mapOfCurrencies.put(country_key, Currency.getInstance(locale));
        }
        return mapOfCurrencies.get(country).toString();
    }

    public void prepareMapOfCountries() {
        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);
            mapOfCountries.put(locale.getDisplayCountry(), iso);
        }
    }

    public String getCountryCode(String country) {
        return mapOfCountries.get(country);
    }

    /**
     * Zwraca informację o pogodzie w podanym mieście danego kraju w formacie JSON (to ma być pełna informacja uzyskana z serwisu openweather - po prostu tekst w formacie JSON).
     * @param city
     * @return
     */
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
        String baseCurrencyCode = getCurrencyCode(country);

        String url = String.format("https://api.exchangerate.host/convert?from=%s&to=%s", currencyCode, baseCurrencyCode);
        String data;

        try {
            data = makeRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

       return Double.valueOf(data);
    }

    public Double getNBPRate(String table) {
        String currencyCode = getCurrencyCode(country);

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
