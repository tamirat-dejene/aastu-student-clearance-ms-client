package g3.scms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import org.kordamp.bootstrapfx.BootstrapFX;

public class LandingPageController {

  @FXML private Button aboutUs;
  @FXML private Button forgotPassword;
  @FXML private TextField idNumber;
  @FXML private AnchorPane inputFieldAnchorPane;
  @FXML private AnchorPane landing_page;
  @FXML private PasswordField password;
  @FXML private Button signUp;
  @FXML private Button submit;
  
  @FXML void handleAboutUs(ActionEvent event) {

  }

  @FXML void handleForgotPassword(ActionEvent event) {

  }

  @FXML void handleResetPassword(KeyEvent event) {

  }

  @FXML
  void handleSignUp(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/signup_page.fxml"));
        AnchorPane signUpPane = loader.load();
        signUpPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        
        AnchorPane.setTopAnchor(signUpPane, 0.0);
        AnchorPane.setBottomAnchor(signUpPane, 0.0);
        AnchorPane.setLeftAnchor(signUpPane, 0.0);
        AnchorPane.setRightAnchor(signUpPane, 0.0);
        
        inputFieldAnchorPane.getChildren().clear();
        inputFieldAnchorPane.getChildren().add(signUpPane);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
  }

  @FXML void handleSubmit(ActionEvent event) {

  }

}
