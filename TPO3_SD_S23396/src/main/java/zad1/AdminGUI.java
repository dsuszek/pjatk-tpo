package zad1;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class AdminGUI extends Application {

    private VBox pane;
    private Admin admin;


    @Override
    public void start(Stage stage) throws UnknownHostException {

        admin = new Admin(InetAddress.getByName("localhost"), 10000);
        Button showAllTopicsBtn = new Button("Show all topics");

        TextField addTopicName = new TextField();
        addTopicName.setPromptText("Please enter name of topic you would like to add");
        addTopicName.setMaxWidth(300);
        TextField addTopicDescription = new TextField();
        addTopicDescription.setPromptText("Please enter description of topic you would like to add");
        addTopicDescription.setMaxWidth(300);

        TextField removeTopicName = new TextField();
        removeTopicName.setPromptText("Please enter name of topic you would like to remove");
        removeTopicName.setMaxWidth(300);


        Button addTopicBtn = new Button("Add topic");
        Button removeTopicBtn = new Button("Remove topic");
        Button informClientsAboutChangesBtn = new Button("Inform clients about changes");


        Map<String, String> mainServerData = new HashMap<>();
        mainServerData.put("sport", "fwfwefwg");

        ComboBox<String> topicsComboBox = new ComboBox<>(FXCollections.observableArrayList(mainServerData.keySet()));




        // action "remove topic" - code 1 (2 parameters)
        removeTopicBtn.setOnAction((ActionEvent e1) -> {
            try {
                admin.sendRequest("1 " + removeTopicName.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // action "add topic" - code 2 (3 parameters)
        addTopicBtn.setOnAction((ActionEvent e1) -> {
            try {
                admin.sendRequest("2 " + addTopicName.getText() + " " + addTopicDescription.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // action "show all topics" - code 5 (1 parameter)
        showAllTopicsBtn.setOnAction((ActionEvent e1 ) -> {
            try {
                admin.sendRequest("5");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pane = new VBox(3);
        pane.setPadding(new Insets(3));
        pane.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(
                topicsComboBox,
                showAllTopicsBtn,
                addTopicName,
                addTopicDescription,
                addTopicBtn,
                removeTopicName,
                removeTopicBtn,
                informClientsAboutChangesBtn
        );

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

}
