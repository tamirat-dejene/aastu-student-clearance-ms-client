package g3.scms.controllers;

import g3.scms.api.Api;
import g3.scms.model.Login;
import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Validate;
import g3.scms.utils.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

public class LoginPageController {
  @FXML private Button forgotPassword;
  @FXML private TextField idNumber;
  @FXML private Label idNumberError;
  @FXML private Label id_number_label;
  @FXML private AnchorPane loginAnchorPane;
  @FXML private Label login_label;
  @FXML private PasswordField password;
  @FXML private Label passwordError;
  @FXML private Label password_label;
  @FXML private Button signUp;
  @FXML private Button submit;

  @FXML void handleAdminBtn(ActionEvent event) {
    try {
      AnchorPane adminPageAp = (AnchorPane) Views.loadFXML("/views/admin_pages/admin_login");
      Views.paintPage(adminPageAp, loginAnchorPane, 0, 0, 0, 0);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
  
  @FXML void handleForgotPassword(ActionEvent event) {
    try {
      AnchorPane forgotPage = (AnchorPane) Views.loadFXML("/views/otp/forgot_password_page");
      Views.paintPage(forgotPage, loginAnchorPane, 50, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @FXML void handleSignUp(ActionEvent event) {
    try {
      AnchorPane signUpPane = (AnchorPane) Views.loadFXML("/views/signup_page");
      Views.paintPage(signUpPane, loginAnchorPane, 0, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @FXML void handleSubmit(ActionEvent event) {
    // Read the user input
    String id = idNumber.getText();
    String pw = password.getText();

    // Make some input checking(validation)
    try {
      Validate.idNumber(id);
    } catch (Error e) {
      idNumberError.setText(e.getMessage());
      return;
    }
    idNumberError.setText("");
    try {
      Validate.password(pw);
    } catch (Error e) {
      passwordError.setText(e.getMessage());
      return;
    }
    passwordError.setText("");

    // After validation send the request
    Login model = new Login();
    model.setIdNumber(id.toUpperCase());
    model.setPassword(pw);

    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
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
      // We will save the session: everytime the client sends the request this result
      // will be attached for the authentication
      File authFile = new File("src/main/resources/auth.bat");
      if (!authFile.exists()) {
        try {
          authFile.createNewFile();
          FileOutputStream output = new FileOutputStream(authFile);
          output.write(auth.get().getBytes());
          output.close();
        } catch (IOException | NoSuchElementException e) {
          Views.displayAlert(AlertType.WARNING, "Permission Error", "Access to file required",
              "Please grant access to the file");
          return null;
        }
      }

      // Successfully logged the user in.
      return res;
    });

    // Reuest was not succesfull
    if (requestResult == null)
      return;

    // Request was succesfull. We can proceed to the main functionalities
    try {
      AnchorPane mainPage = (AnchorPane) Views.loadFXML("/views/main_page");
      AnchorPane homePic = (AnchorPane) Views.loadFXML("/views/home");
      AnchorPane lap = getLeftAnchorPane(mainPage);
      Views.paintPage(homePic, lap, 0, 0, 0, 0);
      Views.paintPage(mainPage, loginAnchorPane, 0, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private AnchorPane getLeftAnchorPane(AnchorPane ap) {
    SplitPane pane = (SplitPane) ap.getChildren()
        .filtered(node -> node.getId() != null && node.getId().equals("splitPane")).get(0);
    AnchorPane lap = (AnchorPane) pane.getItems()
        .filtered(node -> node.getId() != null && node.getId().equals("leftAnchorPane")).get(0);
    return lap;
  }

  public static void main(String[] args) {
  }
}
