package zad1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientGUI extends Application {


    private VBox pane;
    private String[] topicsTest = new String[]{"Sport", "Food"};
    private List<String> listOfSubscribedTopics = Arrays.stream(topicsTest).toList();


    @Override
    public void start(Stage stage) {

        ComboBox<String> topicsComboBox = new ComboBox<>(FXCollections.observableArrayList(listOfSubscribedTopics));
        TextField addTopicName = new TextField();
        addTopicName.setPromptText("Please enter name of topic you would like to subscribe.");
        addTopicName.setPrefSize(100, 30);

        TextField removeTopicName = new TextField();
        removeTopicName.setPromptText("Please enter name of topic you would like to unsubscribe.");
        removeTopicName.setPrefSize(100, 30);

        Button subscribeTopicButton = new Button("Subscribe topic");
        Button unsubscribeTopicButton = new Button("Unsubscribe topic");

        subscribeTopicButton.setOnAction((ActionEvent e1) -> {


        });


        unsubscribeTopicButton.setOnAction((ActionEvent e2) -> {
            String keyTopicToBeUnsubscribed = topicsComboBox.getValue();

            listOfSubscribedTopics.remove(keyTopicToBeUnsubscribed);
        });


        pane = new VBox(10);
        pane.setPadding(new Insets(10));

        pane.getChildren().addAll(
                topicsComboBox,
                subscribeTopicButton,
                unsubscribeTopicButton
        );

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
