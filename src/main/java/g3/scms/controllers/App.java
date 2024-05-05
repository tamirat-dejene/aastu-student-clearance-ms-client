package g3.scms.controllers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.kordamp.bootstrapfx.BootstrapFX;

import g3.scms.utils.Views;

/**
 * Author: Group 3
 */

public class App extends Application {
    private static Scene scene;
    @Override
    public void start(Stage stage) {
        try {
            scene = new Scene(Views.loadFXML("/views/landing_page"), 1080, 540);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("Student Clearance");
            stage.getIcons().add(new Image(App.class.getResource("/image/aastu-logo.jpg").toString()));

            stage.show();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}