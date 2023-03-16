package zad2;

import java.util.*;

public class CurrencyCodes {

    public CurrencyCodes() {
    }

    private static Map<String, Currency> mapOfCurrencies = new HashMap<>();

    public void prepareMapOfCurrencies() {
        for (Locale locale : Locale.getAvailableLocales()) {
            String country = locale.getCountry();
            mapOfCurrencies.put(country, Currency.getInstance(locale));
        }
    }

    public static String getCurrencyCode(String country) {
        return mapOfCurrencies.get(country).toString();
    }
}
