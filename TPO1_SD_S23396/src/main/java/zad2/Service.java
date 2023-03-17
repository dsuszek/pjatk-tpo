package zad2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Service {

    private Map<String, String> mapOfCountries = new HashMap<>();
    private static Map<String, Currency> mapOfCurrencies = new HashMap<>();
    private String country;
    private final String xmlNBPTableA = "https://static.nbp.pl/dane/kursy/xml/a053z230316.xml";
    private final String xmlNBPTableB = "https://static.nbp.pl/dane/kursy/xml/b011z230315.xml";

    // Constructor with one parameter - name of country
    public Service(String country) {
        this.country = country;
    }

    /**
     * Zwraca informację o pogodzie w podanym mieście danego kraju w formacie JSON (to ma być pełna informacja uzyskana z serwisu openweather - po prostu tekst w formacie JSON).
     *
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

    public String getCurrCode(String country) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry().equals(country)) {
                Currency currency = Currency.getInstance(locale);

                return currency.getCurrencyCode();
            }
        }
        return null;
    }

    public Double getRateFor(String currencyCode) {
        String baseCurrencyCode = getCurrCode(country);

        String url = String.format("https://api.exchangerate.host/convert?from=%s&to=%s", currencyCode, baseCurrencyCode);
        String json;

        try {
            json = Connection.makeRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            List<String> filteredJSON = root
                    .findValuesAsText("rate")
                    .stream()
                    .filter(rate -> Integer.parseInt(rate) > 0)
                    .toList();

            String rate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredJSON);

            return Double.valueOf(rate);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("This is not a number.");
        }

        return null;
    }

    public Double getNBPRate() {
        String currencyCode = getCurrCode(country);

        // When we compare PLN to PLN, rate will always be 1.0
        if (currencyCode.equals("PLN")) {
            return 1d;
        }

        try {
            FileInputStream fileIS = new FileInputStream(xmlNBPTableA);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(fileIS);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "kurs_sredni";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            return Double.valueOf(String.valueOf(nodeList));
        } catch (FileNotFoundException e1) {
            System.out.println("XML table couldn't be loaded.");
            e1.printStackTrace();
        } catch (ParserConfigurationException e2) {
            System.out.println("Parser configuration error.");
            e2.printStackTrace();
        } catch (IOException e3) {
            System.out.println("IO Exception");
            e3.printStackTrace();
        } catch (XPathExpressionException e4) {
            System.out.println("X path incorrect");
            e4.printStackTrace();
        } catch (SAXException e5) {
            e5.printStackTrace();
        }

        return null;
    }
}
