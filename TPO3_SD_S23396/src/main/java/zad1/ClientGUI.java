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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class ClientGUI extends Application {


    private VBox pane;
    private Client client;

    @Override
    public void start(Stage stage) throws UnknownHostException {

        client = new Client(InetAddress.getByName("localhost"), 10000);

        TextField addTopicName = new TextField();
        addTopicName.setPromptText("Please enter name of topic you would like to subscribe.");
        addTopicName.setPrefSize(100, 30);

        TextField removeTopicName = new TextField();
        removeTopicName.setPromptText("Please enter name of topic you would like to unsubscribe.");
        removeTopicName.setPrefSize(100, 30);

        Button subscribeTopicButton = new Button("Subscribe topic");
        Button unsubscribeTopicButton = new Button("Unsubscribe topic");
        Button showAllTopicsBtn = new Button("Show all topics");

        ListView listView = new ListView(FXCollections.observableList(client.getListOfAvailableTopics()));
        listView.setEditable(true);
        listView.setPrefSize(100, 100);

        subscribeTopicButton.setOnAction((ActionEvent e1) -> {
            client.subscribeTopic(addTopicName.getText());
            try {
                client.sendRequest("6 " + addTopicName.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        unsubscribeTopicButton.setOnAction((ActionEvent e2) -> {
            String keyTopicToBeUnsubscribed = (String) listView.getSelectionModel().getSelectedItem();

            client.unsubscribeTopic(keyTopicToBeUnsubscribed);
        });

        // action "show all topics" - code 5 (1 parameter)
        showAllTopicsBtn.setOnAction((ActionEvent e1 ) -> {
            try {
                client.sendRequest("5");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        pane = new VBox(10);
        pane.setPadding(new Insets(10));

        pane.getChildren().addAll(
                listView,
                subscribeTopicButton,
                unsubscribeTopicButton,
                showAllTopicsBtn
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
