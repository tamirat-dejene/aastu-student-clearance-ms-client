package g3.scms.controllers;

import java.io.IOException;

import g3.scms.api.Api;
import g3.scms.model.AdminMessage;
import g3.scms.model.Message;
import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Validate;
import g3.scms.utils.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SendNotifController {
  // Send Notification controls
  @FXML private TextArea messageTextField;
  @FXML private TextField receiverId;
  @FXML private MenuButton receiverMenuBtn;
  String selectedReceiverString = null;

  @FXML
  void allStudentChoiceListener(ActionEvent event) {
    selectedReceiverString = "ALL";
    receiverMenuBtn.setText("All Students");
  }

  @FXML
  void oneStudentChoiceListener(ActionEvent event) {
    if (receiverId == null || receiverId.getText().isBlank()) {
      Views.displayAlert(AlertType.ERROR, "Input Error", "Empty Receiver", "Please fill in a receiver id");
      return;
    }
    try {
      selectedReceiverString = receiverId.getText();
      Validate.idNumber(selectedReceiverString);
    } catch (Error e) {
      Views.displayAlert(AlertType.ERROR, "Input Error", "Invalid receiver Id", "Please fill in a correct receiver id");
      return;
    }
    receiverMenuBtn.setText(selectedReceiverString);
  }

  @FXML
  void handleCancelSendingBtn(ActionEvent event) {
    try {
      Button button = (Button) event.getSource();
      AnchorPane mainPane = (AnchorPane) Views.loadFXML("/views/admin_pages/mainPage");
      AnchorPane greatGrandParent = (AnchorPane) button.getParent().getParent().getParent().getParent();
      Views.paintPage(mainPane, greatGrandParent, 0, 0, 0, 0);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void handleConfirmSendingBtn(ActionEvent event) {
    // Check if receiver is null or not
    if (selectedReceiverString == null) {
      Views.displayAlert(AlertType.WARNING, "Error", "Receiver field can't be empty", "Please select a receiver");
      return;
    }
    if (messageTextField.getText() == null || messageTextField.getText().trim().isBlank()) {
      Views.displayAlert(AlertType.WARNING, "Error", "Message field can't be empty", "Please provide messages");
      return;
    }

    String message = messageTextField.getText().trim();
    String authToken = null;
    try {
      authToken = ReqRes.getAuthenticationString();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    AdminMessage adminMessage = new AdminMessage(selectedReceiverString, message);

    // Prepare the request
    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/admin/message");
    request.setHeaderMap("Authorization", authToken);
    request.setJsonBody(ReqRes.makeJsonString(adminMessage));

    // Send the request to the server
    Api api = new Api();
    api.post(request, (error, response) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Error", "Connection/Server Error", error.getMessage());
        return null;
      }
      Message respMessage = (Message) ReqRes.makeModelFromJson(response.body(), Message.class);
      Views.displayAlert(AlertType.INFORMATION, "Response", "Request result", respMessage.getMessage());
      return response;
    });
  }
}
