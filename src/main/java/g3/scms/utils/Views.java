package g3.scms.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

public class Views {
  public static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Views.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /**
   * Displays the Alert with the provided alert typ, title, header text and the message
   * @param alertTpe Alert.AlertType  alert type constant
   * @param title alert title
   * @param headerText alert header text
   * @param message alert message
   * @return the display alert
   */
  public static Alert displayAlert(Alert.AlertType alertTpe, String title, String headerText, String message) {
    Alert alert = new Alert(alertTpe);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);

    // Style the dialog pane
    alert.getDialogPane().setStyle("-fx-background: linear-gradient(to right, #355c7d, #6c5b7b, #c06c84);");
    alert.getDialogPane().getStylesheets().add("s");
    alert.showAndWait();
    return alert;
  }

  /**
   * Paints the new anchor pane onto the given parent ancher pane with the provided offsets
   * @param newPane the new anchorpane to be painted
   * @param parentPane the pane to be painted on
   * @param top the top offset
   * @param btm the bottom offset
   * @param left the left offset
   * @param right the right offset
   */
  public static void paintPage(AnchorPane newPane, AnchorPane parentPane, double top, double btm, double left,
      double right) {
    AnchorPane.setTopAnchor(newPane, top);
    AnchorPane.setBottomAnchor(newPane, btm);
    AnchorPane.setLeftAnchor(newPane, left);
    AnchorPane.setRightAnchor(newPane, right);

    parentPane.getChildren().clear();
    parentPane.getChildren().add(newPane);
  }
}
