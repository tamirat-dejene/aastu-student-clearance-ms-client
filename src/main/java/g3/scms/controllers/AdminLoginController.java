package g3.scms.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import g3.scms.api.Api;
import g3.scms.model.Login;
import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Validate;
import g3.scms.utils.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class AdminLoginController {
  @FXML private Button adminLoginBtn;
  @FXML private TextField deptIdTextField;
  @FXML private PasswordField deptPasswordTextField;
  
  @FXML
  void handleAdminLogin(ActionEvent event) {
    String departmentId = deptIdTextField.getText();
    String adminPassword = deptPasswordTextField.getText();

    try {
      Validate.departmentId(departmentId);
      Validate.departmentPwd(adminPassword);
    } catch (Error e) {
      Views.displayAlert(AlertType.ERROR, "Incorrect credentials", "Wrong inputs provided.",
          "Please insert your valid credential info.");
      return;
    }

    // Send login request
    Login login = new Login();
    login.setIdNumber(departmentId);
    login.setPassword(adminPassword);

    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/auth/login/admin");
    request.setJsonBody(ReqRes.makeJsonString(login));

    Api api = new Api();
    var response = api.post(request, (error, res) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Server/Connection Error", "", error.getMessage());
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

    if (response == null)
      return;

    // Load the main admin page
    try {
      Button button = (Button) event.getSource();
      AnchorPane parent = (AnchorPane) button.getParent();
      AnchorPane mainPane = (AnchorPane) Views.loadFXML("/views/admin_pages/mainPage");
      Views.paintPage(mainPane, parent, 0, 0, 0, 0);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void handleBackToHome(ActionEvent event) {
    try {
      Button button = (Button) event.getSource();
      AnchorPane parent = (AnchorPane) button.getParent();
      AnchorPane loginPane = (AnchorPane) Views.loadFXML("/views/login_page");
      Views.paintPage(loginPane, parent, 0, 0, 0, 0);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
