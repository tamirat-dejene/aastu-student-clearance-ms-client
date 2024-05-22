package g3.scms.controllers;

import java.io.IOException;

import g3.scms.api.Api;
import g3.scms.model.Message;
import g3.scms.model.Request;
import g3.scms.model.UpdatePassword;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Validate;
import g3.scms.utils.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class AccountController {
  @FXML private AnchorPane notifyAnchorPane;
  @FXML private AnchorPane changePwdAnchorPane;
  
  @FXML private PasswordField confirmNewPassword;
  @FXML private PasswordField newPassword;
  @FXML private PasswordField oldPassword;
  @FXML private Label errorLabel;

  @FXML void handleChangePwdBtn(ActionEvent event) {
    // Read the input and validate
    String oldPwd = oldPassword.getText();
    String newPwd = newPassword.getText();
    String confirmPwd = confirmNewPassword.getText();

    try {
      Validate.password(oldPwd);
      Validate.password(newPwd);
      Validate.password(confirmPwd);
    } catch (Error e) {
      errorLabel.setText(e.getMessage());
      return;
    }
    errorLabel.setText("");

    // Confirm if the confirmPwd != newPwd
    if (!newPwd.equals(confirmPwd)) {
      Views.displayAlert(AlertType.ERROR, "Wrong Input", null,
          "Your confirmation password doesn't match the new password");
      return;
    }

    UpdatePassword update = new UpdatePassword();
    update.setNewPassword(newPwd);
    update.setOldPassword(oldPwd);
    update.setConfirm(confirmPwd);
    String json = ReqRes.makeJsonString(update);

    // Send change password request
    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/account/updatepw");
    request.setJsonBody(json);
    try {
      request.setHeaderMap("Authorization", ReqRes.getAuthenticationString());
    } catch (IOException e) { }
    
    // Send put request
    Api api = new Api();
    api.put(request, (error, response) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Server can't be reached", null, error.getMessage());
        return null;
      }

      Message message = (Message)ReqRes.makeModelFromJson(response.body(), Message.class);
      if (response.statusCode() == 202) {
        Views.displayAlert(AlertType.INFORMATION, null, "Success", message.getMessage());
      } else {
        Views.displayAlert(AlertType.ERROR, "", "", message.getMessage());
      }
      return response;
    });

  }
}
