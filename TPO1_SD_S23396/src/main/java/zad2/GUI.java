package zad2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;

public class GUI extends Application {

    private WebEngine engine;
    private Pane pane;
    private final String[] countriesList = getCountriesList();
    private final String[] currenciesList = getCurrenciesList();

    @Override
    public void start(Stage stage) {

        ComboBox<String> countriesComboBox = new ComboBox<String>(FXCollections.observableArrayList(countriesList));
        ComboBox<String> currenciesComboBox = new ComboBox<String>(FXCollections.observableArrayList(currenciesList));
        TextField cityInput = new TextField();
        Button confirmButton = new Button("Confirm");

        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.getChildren().addAll(countriesComboBox, currenciesComboBox, confirmButton);

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("My app");
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

        Collections.sort(countriesList);

        return countriesList
                .toArray(new String[countriesList.size()]);
    }

    private String[] getCurrenciesList() {
        ArrayList<String> currenciesList = new ArrayList<String>();

        for (Currency currency : Currency.getAvailableCurrencies()) {
            currenciesList.add(currency.getCurrencyCode());
        }

        Collections.sort(currenciesList);

        return currenciesList
                .toArray(new String[currenciesList.size()]);
    }


}