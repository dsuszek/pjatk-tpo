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

import java.util.*;

public class ClientGUI extends Application {


    private VBox pane;
    private Map<String, String> mainServerData = Server.getMainServerData();
    private List<String> listOfAvailableTopics = new ArrayList<>();
    private List<String> listOfSubscribedTopics = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        listOfAvailableTopics.addAll(mainServerData.keySet());

        TextField addTopicName = new TextField();
        addTopicName.setPromptText("Please enter name of topic you would like to subscribe.");
        addTopicName.setPrefSize(100, 30);

        TextField removeTopicName = new TextField();
        removeTopicName.setPromptText("Please enter name of topic you would like to unsubscribe.");
        removeTopicName.setPrefSize(100, 30);

        Button subscribeTopicButton = new Button("Subscribe topic");
        Button unsubscribeTopicButton = new Button("Unsubscribe topic");

        ListView listView = new ListView(FXCollections.observableList(listOfAvailableTopics));
        listView.setEditable(true);
        listView.setPrefSize(100, 100);

        subscribeTopicButton.setOnAction((ActionEvent e1) -> {
            listOfSubscribedTopics.add(addTopicName.getText());
        });


        unsubscribeTopicButton.setOnAction((ActionEvent e2) -> {
            String keyTopicToBeUnsubscribed = (String) listView.getSelectionModel().getSelectedItem();

            listOfSubscribedTopics.remove(keyTopicToBeUnsubscribed);
        });


        pane = new VBox(10);
        pane.setPadding(new Insets(10));

        pane.getChildren().addAll(
                listView,
                subscribeTopicButton,
                unsubscribeTopicButton
        );

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Client");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
