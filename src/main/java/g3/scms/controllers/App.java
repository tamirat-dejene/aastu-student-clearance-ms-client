package g3.scms.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import org.kordamp.bootstrapfx.BootstrapFX;

/**
 * Athor: Group 3
 */

public class App extends Application {

    private static Scene scene;

    @Override
    @SuppressWarnings("exports")
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("/views/landing_page"), 1080, 540);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Student Clearance");
        stage.getIcons().add(new Image(App.class.getResource("/image/aastu-logo.jpg").toString()));
        
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
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