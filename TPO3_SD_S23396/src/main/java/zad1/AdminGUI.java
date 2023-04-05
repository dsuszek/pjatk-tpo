package zad1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AdminGUI extends Application {

    private VBox pane;
    private final String[] listOfTopics = new String[]{"Business", "Food", "Travels"};

    @Override
    public void start(Stage stage) {

        // ComboBox<String> countriesComboBox = new ComboBox<String>(FXCollections.observableArrayList(countriesList));
        ComboBox<String> topicsComboBox = new ComboBox<>(FXCollections.observableArrayList(listOfTopics));
        TextField countryNameInput = new TextField();
        TextField cityNameInput = new TextField();
        Button confirmButton = new Button("Confirm");
        GridPane results = new GridPane();
        Label weather = new Label();
        Label currencyRate = new Label();
        Label NBPRate = new Label();

        countryNameInput.setPromptText("Please enter name of topic.");
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
                topicsComboBox,
                confirmButton);

        confirmButton.setOnAction((ActionEvent e) -> {

            weather.setText("");
            currencyRate.setText("");
            NBPRate.setText("");

            String country = countryNameInput.getText();
            String currency = topicsComboBox.getValue();
            String city = cityNameInput.getText();

        });

        pane.getChildren().addAll(
                new Label("Weather: "),
                weather,
                new Label("Exchange rate: "),
                currencyRate,
                new Label("NBP exchange rate: "),
                NBPRate
        );

        Scene scene = new Scene(pane, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
