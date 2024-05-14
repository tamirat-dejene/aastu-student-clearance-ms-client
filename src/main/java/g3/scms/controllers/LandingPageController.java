package g3.scms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.kordamp.bootstrapfx.BootstrapFX;

import g3.scms.api.Api;
import g3.scms.model.Login;
import g3.scms.model.Message;
import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Validate;
import g3.scms.utils.Views;


public class LandingPageController {

  @FXML private Button aboutUs;
  @FXML private Button forgotPassword;
  @FXML private TextField idNumber;
  @FXML private TextField emailAddress;
  @FXML private AnchorPane inputFieldAnchorPane;
  @FXML private AnchorPane resetpasswordAnchorPane;
  @FXML private AnchorPane landing_page;
  @FXML private PasswordField password;
  @FXML private Button signUp;
  @FXML private Button submit;
  
  @FXML private Label idNumberError;
  @FXML private Label passwordError;
  @FXML private Label resetPageInputError;

  @FXML void handleAboutUs(ActionEvent event) {
    Alert alert = Views.displayAlert(AlertType.INFORMATION, "Team Innov8", "Developers",
        "Developed by AASTU student for Advanced Programming final project");
    alert.getDialogPane().setStyle("-fx-background-color: yellow;");
  }

  @FXML
  void handleForgotPassword(ActionEvent event) {
    try {
      AnchorPane forgotPage = (AnchorPane) Views.loadFXML("/views/forgot_password_page");
      forgotPage.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
      Views.paintPage(forgotPage, inputFieldAnchorPane, 50, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @FXML
  void handleConfirmResetBtn(ActionEvent event) {
    System.out.println(event.getSource());
    String emailInput = emailAddress.getText();
    String idNumberInput = idNumber.getText();
    try {
      Validate.email(emailInput);
      Validate.idNumber(idNumberInput);
    } catch (Error e) {
      resetPageInputError.setText(e.getMessage());
      return;
    }
    resetPageInputError.setText("");
    
    // prepare email json
    Message message = new Message();
    message.setMessage(emailInput);
    String emailJson = ReqRes.makeJsonString(message);

    // Prepare the request to be sent
    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/account/resetpw");
    request.setHeaderMap("Authorization", idNumberInput);
    request.setJsonBody(emailJson);

    // PUT the request
    Api api = new Api();
    var response = api.put(request, (err, res) -> {
      if (err != null) {
        Views.displayAlert(AlertType.ERROR, "System Error", "Can't reach the server!", err.getMessage());
        return null;
      }

      // If the request is accepted/status code 202
      if(res != null && res.statusCode() == 202) return res;
      
      // Request not accepted or server error
      Message respMessage = (Message) ReqRes.makeModelFromJson(res.body(), Message.class);
      switch (res.statusCode()) {
        case 400:
          Views.displayAlert(AlertType.ERROR, "Bad request", "", respMessage.getMessage()); 
          break;
        case 500:
        default:
          Views.displayAlert(AlertType.ERROR, "Server error", "", respMessage.getMessage()); 
          break;
      }
      return null;
    });
    
    if(response == null) return;
    System.out.println("Success: " + response.body());
  }
  
  @FXML
  void handleSignUp(ActionEvent event) {
    try {
      AnchorPane signUpPane = (AnchorPane) Views.loadFXML("/views/signup_page");
      signUpPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
      Views.paintPage(signUpPane, inputFieldAnchorPane, 0, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @FXML
  void backToHome(ActionEvent event) {
    try {
      AnchorPane loginPane = (AnchorPane) Views.loadFXML("/views/login_page");
      loginPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
      
      Views.paintPage(loginPane, (AnchorPane)resetpasswordAnchorPane.getParent(), 0, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @FXML
  void handleSubmit(ActionEvent event) {
    // Read the user input
    String id = idNumber.getText();
    String pw = password.getText();

    // Make some input checking(validation)
    try { Validate.idNumber(id);
    } catch (Error e) { idNumberError.setText(e.getMessage()); return; }
    idNumberError.setText("");
    try { Validate.password(pw); 
    } catch (Error e) { passwordError.setText(e.getMessage()); return; }
    passwordError.setText("");

    // After validation send the request
    Login model = new Login();
    model.setIdNumber(id);
    model.setPassword(pw);

    Request request = new Request();
    request.setBaseUrl("http://localhost:1492/");
    request.setPath("api/auth/login");
    request.setJsonBody(ReqRes.makeJsonString(model));

    Api api = new Api();
    var requestResult = api.post(request, (err, res) -> {
      if (err != null) {
        Views.displayAlert(AlertType.ERROR, "System Error", "Some Internal Error", err.getMessage());
        return null;
      }

      var statusCode = res.statusCode();
      if (statusCode != 200) {
        Views.displayAlert(AlertType.WARNING, "Bad Request", "Something is wrong with the request",
            "Incorrect username/password");
        return null;
      }

      // The user is authenticated, then!
      var auth = res.headers().firstValue("Authorization");      
      // We will save the session: everytime the client sends the request this result will be attached for the authentication
      File authFile = new File("aastu_scms/src/main/resources/auth.bat");
      if (!authFile.exists()) {
        try {
          authFile.createNewFile();
          FileOutputStream output = new FileOutputStream(authFile);
          output.write(auth.get().getBytes());
          output.close();
        } catch (IOException | NoSuchElementException e) {
          Views.displayAlert(AlertType.WARNING, "Permission Error", "Access to file required",
              "Please grant access to file");
          return null;
        }
      }
      
      // Successfully logged the user in.
      return res;
    });

    // Reuest was not succesfull
    if(requestResult == null) return;

    // Request was succesfull. We can proceed to the main functionalities
    try {
      AnchorPane mainPage = (AnchorPane) Views.loadFXML("/views/main_page");
      mainPage.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
      Views.paintPage(mainPage, inputFieldAnchorPane, 0, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

}
