package g3.scms.utils;

import java.io.IOException;
import java.net.URISyntaxException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

public class Views {
  /**
   * Loads the fxml file using getResource method
   * @param fxml the file path relative to the resource folder
   * @return loaded object heirarchy(Parent)
   * @throws IOException if the file is not found
   */
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
  public static Alert displayAlert(Alert.AlertType alertType, String title, String headerText, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);

    var dialogPane = alert.getDialogPane();
    try {
      dialogPane.getStylesheets().add(Views.class.getResource("/styles/dialogpane.css").toURI().toString());
    } catch (URISyntaxException e) { }

    switch (alertType) {
      case INFORMATION:
        dialogPane.getStyleClass().add("information");
        break;
      case WARNING:
        dialogPane.getStyleClass().add("warning");
        break;
      case ERROR:
        dialogPane.getStyleClass().add("error");
        break;
      case CONFIRMATION:
        dialogPane.getStyleClass().add("confirmation");
        break;
      default:
        break;
    }
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
