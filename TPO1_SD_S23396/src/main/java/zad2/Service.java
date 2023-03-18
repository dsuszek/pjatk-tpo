package zad2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
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
    private String country = null;
    private final String NBP_XML_BASIS_A = "https://static.nbp.pl/dane/kursy/xml/a054z230317.xml";
    private final String NBP_XML_BASIS_B = "https://static.nbp.pl/dane/kursy/xml/b011z230315.xml";

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
        String data;

        try {
            data = Connection.makeRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode json = mapper.readTree(data);

            return json.get("weather").get(0).get("description").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
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
            double rate = root.path("info").path("rate").asDouble();

            return rate;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("This is not a number.");
        }

        return null;
    }

    public Double getNBPRate(String path) {
        String currencyCode = getCurrCode(country);

        if (currencyCode == null) {
            return null;
        }

        // When we compare PLN to PLN, rate will always be 1.0
        if (currencyCode.equals("PLN")) {
            return 1.0;
        }

        String xmlData;
        try {
            xmlData = Connection.makeRequest(path);
        } catch (IOException e1) {
            return null;
        }

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlData)));
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = String.format(
                    "/tabela_kursow/pozycja[descendant::kod_waluty[text()=\"%s\"]]/kurs_sredni", currencyCode
            );

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            if (nodeList.getLength() == 0) {
                return null;
            }

            Node node = nodeList.item(0);
            String textValue = node.getTextContent().replace(',', '.');
            Double value = Double.parseDouble(textValue);

            return value;

        } catch (XPathExpressionException e) {
            e.printStackTrace();
            System.out.println("XPathExpressionException");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("FileNotFoundException");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println("ParserConfigurationException");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
        } catch (SAXException e) {
            e.printStackTrace();
            System.out.println("SAXException");
        }

        return null;
    }

    public Double getNBPRate() {
        Double variantA = getNBPRate(NBP_XML_BASIS_A);

        if (variantA != null) {
            return variantA;
        }

        Double variantB = getNBPRate(NBP_XML_BASIS_B);
        if (variantB != null) {
            return variantB;
        }

        return null;
    }
}
