package Zad1;

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

public class AdminGUI extends Application {

    // Declare variables
    private VBox pane;



    @Override
    public void start(Stage stage) throws Exception {
        pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Scene scene = new Scene(pane, 1200, 800);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        AdminGUI.launch(AdminGUI.class, args);
    }
}
