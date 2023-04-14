package zad1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AdminGUI extends Application {


    private VBox pane;


    @Override
    public void start(Stage stage) {

        // Server server = new Server("localhost", 10000);

        Button showAllTopicsBtn = new Button("Show all topics");

        TextField addTopicName = new TextField();
        addTopicName.setPromptText("Please enter name of topic you would like to add");
        addTopicName.setMaxWidth(300);

        TextField removeTopicName = new TextField();
        removeTopicName.setPromptText("Please enter name of topic you would like to remove");
        removeTopicName.setMaxWidth(300);


        Button addTopicBtn = new Button("Add topic");
        Button removeTopicBtn = new Button("Remove topic");
        Button informClientsAboutChangesBtn = new Button("Inform clients about changes");


//        showAllTopicsBtn.setOnAction((ActionEvent e1) -> {
//            server.showAllTopics();
//        });


        addTopicBtn.setOnAction((ActionEvent e1) -> {


        });


        pane = new VBox(3);
        pane.setPadding(new Insets(3));
        pane.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(
                showAllTopicsBtn,
                addTopicName,
                addTopicBtn,
                removeTopicName,
                removeTopicBtn,
                informClientsAboutChangesBtn
        );

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
