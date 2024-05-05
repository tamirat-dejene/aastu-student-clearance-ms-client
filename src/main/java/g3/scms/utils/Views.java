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

  public static Alert displayAlert(Alert.AlertType alertTpe, String title, String headerText, String message) {
    Alert alert = new Alert(alertTpe);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    
    alert.showAndWait();
    return alert;
  }

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
