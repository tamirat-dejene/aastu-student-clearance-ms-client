package g3.scms.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import g3.scms.api.Api;
import g3.scms.model.Message;
import g3.scms.model.Request;
import g3.scms.model.SecurePassword;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Validate;
import g3.scms.utils.Views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ResetPasswordController {
  @FXML private AnchorPane otpAnchorPane;
  @FXML private AnchorPane newPwdAnchorPane;
  @FXML private AnchorPane resetpasswordAnchorPane;
  
  @FXML private TextField idNumber;
  @FXML private TextField emailAddress;
  @FXML private TextField otpTextField;
  @FXML private PasswordField newPwd;
  @FXML private Label resetPageInputError;

  @FXML
  void handleConfirmResetBtn(ActionEvent event) {
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
    idNumberInput = idNumberInput.toUpperCase();
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
        Views.displayAlert(AlertType.ERROR, "System Error", "Server can't be reached at the moment!", err.getMessage());
        return null;
      }

      // If the request is accepted/status code 202
      if (res != null && res.statusCode() == 202)
        return res;

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

    if (response == null)
      return;

    // Load the OTP field page
    try {
      AnchorPane otpPane = (AnchorPane) Views.loadFXML("/views/otp/otp_page");
      Views.paintPage(otpPane, (AnchorPane) resetpasswordAnchorPane.getParent(), 85, 0, 20, 0);
    } catch (IOException e) { }
  }

  @FXML
  void handleConfirmNewPwdBtn(ActionEvent event) {
    String newPassword = newPwd.getText();
    try {
      Validate.password(newPassword);
    } catch (Error e) {
      Views.displayAlert(AlertType.ERROR, "Invalid input", "Provide valid password", e.getMessage());
      return;
    }

    SecurePassword securePassword = SecurePassword.saltAndHashPassword(Util.generateSalt(16), newPassword);
    String newPwdJson = "";
    try {
      newPwdJson = URLEncoder.encode("{\"hashedPassword\":\"" + securePassword.getHashedPassword() + "\"}", "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return;
    }
    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/account/resetpw/?newpwd=" + newPwdJson);

    Api api = new Api();
    api.get(request, (error, response) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Server/Connection Error", "Server can't be reached at the moment", error.getMessage());
        return null;
      }

      // http conflict
      Message message = (Message) ReqRes.makeModelFromJson(response.body(), Message.class);
      if (response != null && response.statusCode() == 500) {
        Views.displayAlert(AlertType.ERROR, "Something went wrong", "", message.getMessage());
        return null;
      }

      // Valid
      Views.displayAlert(AlertType.INFORMATION, "Password reset", "Now you can login", message.getMessage());
      AnchorPane loginPane;
      try {
        loginPane = (AnchorPane) Views.loadFXML("/views/login_page");
        Views.paintPage(loginPane, (AnchorPane) newPwdAnchorPane.getParent(), 0, 0, 0, 0);
      } catch (IOException e) {
      }
      return response;
    });
  }
  
  @FXML
  void handleConfrimOtp(ActionEvent event) {
    String inputOtp = otpTextField.getText();
    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/account/resetpw/?otp=" + inputOtp);

    Api api = new Api();
    var resp = api.get(request, (error, response) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Server/Connection Error", "Error connecting to the server", error.getMessage());
        return null;
      }

      // http conflict
      Message message = (Message) ReqRes.makeModelFromJson(response.body(), Message.class);
      if (response != null && response.statusCode() == 409) {
        Views.displayAlert(AlertType.ERROR, "Incorrect OTP", "Try again later", message.getMessage());
        return null;
      }

      // Valid
      return response;
    });

    if (resp == null)
      return;

    // Load the newpwd page
    try {
      AnchorPane newpwdPane = (AnchorPane) Views.loadFXML("/views/otp/new_pwd_page");
      Views.paintPage(newpwdPane, (AnchorPane) otpAnchorPane.getParent(), 70, 0, 20, 0);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @FXML
  void backToHome(ActionEvent event) {
    try {
      AnchorPane loginPane = (AnchorPane) Views.loadFXML("/views/login_page");
      Views.paintPage(loginPane, (AnchorPane)((Button)event.getSource()).getParent().getParent(), 0, 0, 0, 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
