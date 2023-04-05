package zad1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientGUI extends Application {


    private VBox vBox;


    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(vBox, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
