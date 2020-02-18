package com.neftxx;

import com.neftxx.constant.AritSoftware;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static Stage MAIN_STAGE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, AritSoftware.APP_WIDTH, AritSoftware.APP_HEIGHT);
        primaryStage.getIcons().add(AritSoftware.APP_ICON);
        primaryStage.setTitle(AritSoftware.APP_NAME);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
        MAIN_STAGE = primaryStage;
    }
}
