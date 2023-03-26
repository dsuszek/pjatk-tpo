package zad1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
    private VBox pane;
    private final String[] languages = new String[]{"ENG", "FR", "NL"};


    @Override
    public void start(Stage stage) {

        ComboBox<String> languageChoice = new ComboBox<>(FXCollections.observableArrayList(languages));
        TextField wordToBeTranslatedInput = new TextField();
        TextField languageCode = new TextField();
        Label translation = new Label();
        Button confirmButton = new Button("Translate");

        // Set the parameters of wordToBeTranslatedInput TextField
        wordToBeTranslatedInput.setPromptText("Please enter word to translate.");
        wordToBeTranslatedInput.setPrefSize(100, 30);
        wordToBeTranslatedInput.setOnAction(
                actionEvent -> wordToBeTranslatedInput.getText()
        );

        // Set the parameters of languageCode TextField
        languageCode.setPromptText("Please choose target language code.");
        languageCode.setPrefSize(100, 30);
        languageCode.setOnAction(
                actionEvent -> languageCode.getText()
        );

        pane = new VBox(10);
        pane.setPadding(new Insets(10));
        pane.getChildren().addAll(
                new Label("Enter word to be translated: "),
                wordToBeTranslatedInput,
                new Label("Choose target language code: "),
                languageChoice,
                confirmButton,
                new Label("Translation: ")
        );

        confirmButton.setOnAction((ActionEvent e) -> {
                    wordToBeTranslatedInput.setText("");
                    translation.setText("");

                    String wordToBeTranslated = wordToBeTranslatedInput.getText();
                    translation.setText(Client.getTranslation(wordToBeTranslated, "EN"));
                }
        );

        pane.getChildren().addAll(
                translation
        );


        Scene scene = new Scene(pane, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }
}
