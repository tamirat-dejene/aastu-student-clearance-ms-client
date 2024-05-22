package g3.scms.controllers;

import g3.scms.api.Api;
import g3.scms.model.ClearanceReason;
import g3.scms.model.Message;
import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Validate;
import g3.scms.utils.Views;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.net.HttpURLConnection;

public class ClearanceAppController {
  @FXML private TextField appNumberTextField;
  @FXML private TextField statusResultTextField;
  @FXML private ToggleGroup clearanceReason;
  @FXML private TextArea otherReasonTextArea;

  @FXML
  void handleSendStatusBtn(ActionEvent event) {
    statusResultTextField.setText("");
    // Read the application number
    String applicationNumber = appNumberTextField.getText();

    // Validate application number
    try {
      Validate.appNumber(applicationNumber);
    } catch (Error e) {
      Views.displayAlert(AlertType.ERROR, "Invalid App#", "", "Please enter a valid app number");
      return;
    }

    // Send the request
    Request request = new Request();
    try {
      String sessionToken = ReqRes.getAuthenticationString();
      request.setHeaderMap("Authorization", sessionToken);
    } catch (Exception e) {
      /* Something must be wrong */ }
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/clearance/?applicationNumber=" + applicationNumber);

    // Send the request
    Api api = new Api();
    api.get(request, (error, response) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Something went wrong", "", error.getMessage());
        return null;
      }

      Message message = (Message) ReqRes.makeModelFromJson(response.body(), Message.class);
      if (response.statusCode() == HttpURLConnection.HTTP_ACCEPTED)
        statusResultTextField.setText("Status: " + message.getMessage());
      else
        Views.displayAlert(AlertType.INFORMATION, "Incorrect request", "", message.getMessage());

      return response;
    });
  }
  
  @FXML
  void handleSubmitReasonBtn(ActionEvent event) {
    // Ask for confirmation
    Alert alert = Views.displayAlert(AlertType.CONFIRMATION, "Confirmation", "", "Confirm to proceed");
    if (alert.getResult().getText().equals("Cancel"))
      return;

    // Filter down selected toggle and validate
    var toggles = clearanceReason.getToggles();
    FilteredList<Toggle> selected = toggles.filtered(toggle -> toggle.isSelected());
    if (selected.size() == 0) {
      Views.displayAlert(AlertType.ERROR, "Empty Field",
          "Reason Field can't be empty", "You should select one reason");
      return;
    }

    RadioButton choiceButton = (RadioButton) selected.get(0);
    if (choiceButton.getText().equals("Other") && otherReasonTextArea.getText().isBlank()) {
      Views.displayAlert(AlertType.WARNING, "Invalid input", "Empty other field", "Other field can't be empty!");
      return;
    }

    String reason = choiceButton.getText().equals("Other") ? otherReasonTextArea.getText().trim()
        : choiceButton.getText();
    ClearanceReason clearance = new ClearanceReason();
    clearance.setClearanceReason(reason);

    // Convert to json
    String json = ReqRes.makeJsonString(clearance);

    // Prepare the request
    Request request = new Request();
    // Put the authentication token inside the header
    try {
      String sessionToken = ReqRes.getAuthenticationString();
      request.setHeaderMap("Authorization", sessionToken);
    } catch (Exception e) {
      /* Something must be wrong */ }
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/clearance");
    request.setJsonBody(json);

    // Now we are ready to send the request using post client api
    Api api = new Api();
    api.post(request, (error, response) -> {
      if (error != null) {
        // The server is down or connection error
        Views.displayAlert(AlertType.INFORMATION, "Server/Connection Error", "Something went wrong",
            error.getMessage());
        return null;
      }

      var responseCode = response.statusCode();
      String jsonResponse = response.body();
      Message message = (Message) ReqRes.makeModelFromJson(jsonResponse, Message.class);
      if (responseCode != 201) {
        Views.displayAlert(AlertType.WARNING, "Bad Request", "Something is wrong with the request",
            message.getMessage());
      } else {
        Views.displayAlert(AlertType.INFORMATION, "Application Accepted",
            "Keep your application# U will use to check your appliction status.",
            message.getMessage());
      }
      return response;
    });
  }
}
