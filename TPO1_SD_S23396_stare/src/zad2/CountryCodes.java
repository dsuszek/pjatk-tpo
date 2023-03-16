package zad2;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CountryCodes {

    private static Map<String, String> mapOfCountries = new HashMap<>();

    public void prepareMapOfCountries() {
        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);
            mapOfCountries.put(locale.getDisplayCountry(), iso);
        }
    }

    public static String getCountryCode(String country) {
        return mapOfCountries.get(country);
    }
}
