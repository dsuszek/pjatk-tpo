package zad2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

public class GUI extends Application {

    private WebView webView = new WebView();
    private WebEngine webEngine = webView.getEngine();
    private VBox pane;
    private final String[] countriesList = getCountriesList();
    private final String[] currenciesList = new String[]{"EUR", "PLN", "USD", "AUD", "JPY", "GBP"};


    @Override
    public void start(Stage stage) {

        // ComboBox<String> countriesComboBox = new ComboBox<String>(FXCollections.observableArrayList(countriesList));
        ComboBox<String> currenciesComboBox = new ComboBox<String>(FXCollections.observableArrayList(currenciesList));
        TextField countryNameInput = new TextField();
        TextField cityNameInput = new TextField();
        Button confirmButton = new Button("Confirm");
        GridPane results = new GridPane();
        Label weather = new Label();
        Label currencyRate = new Label();
        Label NBPRate = new Label();

        countryNameInput.setPromptText("Please enter name of country.");
        countryNameInput.setPrefSize(100, 30);
        countryNameInput.setOnAction(
                actionEvent -> countryNameInput.getText()
        );

        cityNameInput.setPromptText("Please enter name of city.");
        cityNameInput.setPrefSize(100, 30);

        pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.getChildren().addAll(
                new Label("Enter country name: "),
                countryNameInput,
                new Label("Enter city name: "),
                cityNameInput,
                new Label("Please select currency code: "),
                currenciesComboBox,
                confirmButton,
                webView);

        confirmButton.setOnAction((ActionEvent e) -> {

            weather.setText("");
            currencyRate.setText("");
            NBPRate.setText("");

            String country = countryNameInput.getText();
            String currency = currenciesComboBox.getValue();
            String city = cityNameInput.getText();

            Service service = new Service(country);
            weather.setText(service.getWeather(city));
            currencyRate.setText(service.getRateFor(currency).toString());

            if(service.getNBPRate() != null) {
                NBPRate.setText(service.getNBPRate().toString());
            }

            Platform.runLater(() -> {
                        webEngine.load("https://en.wikipedia.org/wiki/" + city);
                    }
            );
        });

        pane.getChildren().addAll(
                weather,
                currencyRate
        );

        Scene scene = new Scene(new VBox(pane, results), 1200, 800);
        stage.setScene(scene);
        stage.show();
    }


    private String[] getCountriesList() {
        ArrayList<String> countriesList = new ArrayList<String>();

        for (Locale locale : Locale.getAvailableLocales()) {
            String displayCountry = locale.getDisplayCountry();

            if (!displayCountry.equals("")) {
                countriesList.add(displayCountry);
            }
        }

        countriesList
                .stream()
                .distinct()
                .collect(Collectors.toList());
        Collections.sort(countriesList);

        return countriesList
                .toArray(new String[countriesList.size()]);
    }

    private String[] getCurrenciesList() {
        ArrayList<String> currenciesList = new ArrayList<String>();

        for (Currency currency : Currency.getAvailableCurrencies()) {
            currenciesList.add(currency.getCurrencyCode());
        }

        currenciesList
                .stream()
                .distinct()
                .collect(Collectors.toList());
        Collections.sort(currenciesList);


        return currenciesList
                .toArray(new String[currenciesList.size()]);
    }


}