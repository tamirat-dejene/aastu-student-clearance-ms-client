package g3.scms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;

import g3.scms.utils.Views;

public class LandingPageController {
  @FXML
  void handleAboutUs(ActionEvent event) {
    Views.displayAlert(AlertType.INFORMATION, "Team Innov8", "Developers",
        "Developed by AASTU student for Advanced Programming final project");
  }
}