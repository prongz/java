package com.elltechs._gui_oxy_rev02;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.stage.WindowEvent;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static double A = 1.0;
    private static double B = 0.0;

    public static double getA() {
        return A;
    }

    public static void setA(double A2) {
        A = A2;
    }

    public static double getB() {
        return B;
    }

    public static void setB(double B2) {
        B = B2;
    }
    
    public static Scene getScene() {

        return scene;
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Page001"), 800, 480);
        scene.setCursor(Cursor.NONE);
        stage.setScene(scene);
        stage.show();
          stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}