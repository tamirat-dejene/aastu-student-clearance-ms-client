package g3.scms.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import g3.scms.api.Api;
import g3.scms.model.Notification;
import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainPageController {
  // Main controllers
  @FXML private AnchorPane mainPageAnchorPane;
  @FXML private AnchorPane leftAnchorPane;
  @FXML private AnchorPane rightAnchorPane;
  @FXML private ListView<Notification> listView;
  @FXML private VBox vbox; 

  @FXML
  void handleSubmitApp(ActionEvent event) throws IOException {
    AnchorPane formAnchorPane = (AnchorPane) Views.loadFXML("/views/app/form");
    Views.paintPage(formAnchorPane, leftAnchorPane, 0, 0, 0, 0);
  }

  @FXML
  void handleCheckStatusBtn(ActionEvent event) throws IOException {
    AnchorPane statusAnchorPane = (AnchorPane) Views.loadFXML("/views/app/status_page");
    Views.paintPage(statusAnchorPane, leftAnchorPane, 0, 0, 0, 0);
  }


  @SuppressWarnings("deprecation")
  @FXML
  void handleNotificationBtn(ActionEvent event) throws IOException {
    AnchorPane notifyAnchorPane = (AnchorPane) Views.loadFXML("/views/notification/notify_page");
    Views.paintPage(notifyAnchorPane, rightAnchorPane, 0, 0, 0, 0);

    // Send get request
    Request request = new Request();
    try {
      String sessionToken = ReqRes.getAuthenticationString();
      request.setHeaderMap("Authorization", sessionToken);
    } catch (Exception e) { /* Something must be wrong */ }
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/notification");

    Api api = new Api();
    var resp = api.get(request, (error, response) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Network/Sever Error", "Try again later.", error.getMessage());
        return null;
      }

      if (response.statusCode() >= 400) {
        Views.displayAlert(AlertType.ERROR, "Network/Sever Error", "Try again later.", "Unauthorizes user! Relogin");
        return null;
      }

      if (response.statusCode() == 304)
        return null; // no new notification
      return response; // there is new nitification // 202 status code
    });

    if (resp == null)
      return;

    ObservableList<Notification> notifications = FXCollections.observableArrayList();
    Notification notification = (Notification) ReqRes.makeModelFromJson(resp.body(), Notification.class);
    notifications.add(notification);

    vbox = (VBox) notifyAnchorPane.getChildren()
        .filtered(child -> child.getId() != null && child.getId().equals("vbox")).get(0);
    @SuppressWarnings({ "unchecked", "rawtypes" })
    ListView<Notification> listView = (ListView) vbox.getChildren().filtered(node -> node instanceof ListView).get(0);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(notification);
    listView.getItems().add(new Notification("Profile picture", "Do you have a profile picture?",
        Util.getDateString(new Date(2022, 9, 12, 6, 23, 23))));

    listView.getItems().stream().sorted((n1, n2) -> {
      Date d1 = Util.parseDateString(n1.getDate());
      Date d2 = Util.parseDateString(n2.getDate());
      return d2.compareTo(d1);
    });

    listView.getSelectionModel().selectedItemProperty().addListener((notif, b, c) -> {
      Views.displayAlert(AlertType.INFORMATION, notif.getValue().getTitle(), notif.getValue().getDate(),
          notif.getValue().getMessage());
      try {
        handleNotificationBtn(event);
      } catch (IOException e) {
      }
    });
  }

  @FXML
  void handleChangepassword(ActionEvent event) throws IOException {
    AnchorPane changePwdPane = (AnchorPane) Views.loadFXML("/views/notification/change_pwd_page");
    Views.paintPage(changePwdPane, rightAnchorPane, 0, 0, 0, 0);
  }
  
  @FXML
  void handleLogout(ActionEvent event) throws IOException {
    // Ask for confirmation
    Alert alert = Views.displayAlert(AlertType.CONFIRMATION, "Confirmation", "", "Are you sure do want to logout?");
    if (alert.getResult().getText().equals("Cancel"))
      return;

    // Delete the auth.bat file
    File file = new File("aastu_scms/src/main/resources/auth.bat");
    if (file.exists())
      file.delete();

    // load the login page
    AnchorPane loginPane = (AnchorPane) Views.loadFXML("/views/login_page");
    mainPageAnchorPane.setStyle("-fx-background-color: transparent");
    Views.paintPage(loginPane, mainPageAnchorPane, 0, 0, 0, 0);
  }
}