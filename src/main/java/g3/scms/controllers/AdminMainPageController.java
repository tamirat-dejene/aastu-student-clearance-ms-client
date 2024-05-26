package g3.scms.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;

import g3.scms.api.Api;
import g3.scms.model.ClearAppForJson;
import g3.scms.model.ClearanceApplication;
import g3.scms.model.Message;
import g3.scms.model.Request;
import g3.scms.model.Status;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Views;

public class AdminMainPageController {

  @FXML private TableView<ClearanceApplication> clearanceAppTable;
  @FXML private TableColumn<ClearanceApplication, Integer> applicationIdCol;
  @FXML private TableColumn<ClearanceApplication, String> applicantIdCol;
  @FXML private TableColumn<ClearanceApplication, String> reasonCol;
  @FXML private TableColumn<ClearanceApplication, Status> statusCol;
  @FXML private TableColumn<ClearanceApplication, String> dateCol;

  @FXML private MenuButton newStatusMenuBtn;
  @FXML private AnchorPane mainPageAnchorPane;
  @FXML private AnchorPane lowerAnchorPane;
  @FXML private AnchorPane tableAnchorPane;
  MenuItem selectedStatusMenuItem = null;

  @FXML
  public void initialize() {
    applicationIdCol.setCellValueFactory(new PropertyValueFactory<>("applicationId"));
    applicantIdCol.setCellValueFactory(new PropertyValueFactory<>("applicantId"));
    reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    statusCol.setCellFactory(ComboBoxTableCell.forTableColumn(Status.values()));
    clearanceAppTable.setItems(getApplications());

    clearanceAppTable.setRowFactory(tv -> {
      TableRow<ClearanceApplication> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
          var selected = row.getItem();
          Views.displayAlert(AlertType.INFORMATION, "Reason",
              "Applicant " + selected.getApplicantId(), row.getItem().getReason());
        }
      });
      return row;
    });
  }

  private ObservableList<ClearanceApplication> getApplications() {
    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/admin/applications");
    try {
      String authToken = ReqRes.getAuthenticationString();
      request.setHeaderMap("Authorization", authToken);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return null;
    }

    Api api = new Api();
    var resp = api.get(request, (err, response) -> {
      if (err != null) {
        Views.displayAlert(AlertType.ERROR, "Error", "Connection/Server Error", err.getMessage());
        return null;
      }

      return response;
    });

    if (resp != null && resp.statusCode() == 202) {
      ClearAppForJson[] applications = (ClearAppForJson[]) ReqRes.makeModelFromJson(resp.body(),
          ClearAppForJson[].class);
      ObservableList<ClearanceApplication> obArrList = FXCollections.observableArrayList();
      for (ClearAppForJson clearAppForJson : applications) {
        obArrList.add(clearAppForJson.toClearanceApplication());
      }
      return obArrList;
    }
    return null;
  }

  private void updateStatus(int applicationId, Status newStatus) {
    for (ClearanceApplication clearanceApplication : clearanceAppTable.getItems()) {
      if (clearanceApplication.getApplicationId() == applicationId) {
        clearanceApplication.setStatus(newStatus);
        break;
      }
    }
    clearanceAppTable.refresh();
  }
  private void updateStatus(Status newStatus) {
    for (ClearanceApplication clearanceApplication : clearanceAppTable.getItems())
      clearanceApplication.setStatus(newStatus);
    clearanceAppTable.refresh();
  }

  @FXML void handleApprovedStatus(ActionEvent event) {
    selectedStatusMenuItem = (MenuItem) event.getSource();
    newStatusMenuBtn.setText(selectedStatusMenuItem.getText());
  }

  @FXML void handleDeclinedStatus(ActionEvent event) {
    selectedStatusMenuItem = (MenuItem) event.getSource();
    newStatusMenuBtn.setText(selectedStatusMenuItem.getText());
  }

  @FXML void handlePendingStatus(ActionEvent event) {
    selectedStatusMenuItem = (MenuItem) event.getSource();
    newStatusMenuBtn.setText(selectedStatusMenuItem.getText());
  }

  // Table event
  @FXML
  void handleCommitStatus(TableColumn.CellEditEvent<ClearanceApplication, Status> event) {
    Status newStatus = event.getNewValue();
    Status oldStatus = event.getOldValue();
    int applicationId = event.getTableView().getSelectionModel().getSelectedItem().getApplicationId();
    if (newStatus.equals(oldStatus))
      return;
    
    // Send put request
    String authToken = "";
    try {
      authToken = ReqRes.getAuthenticationString();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return;
    }
    Message newStatusMessage = new Message();
    newStatusMessage.setMessage(newStatus.toString());
    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/admin/status/?applicationId=" + applicationId);

    request.setHeaderMap("Authorization", authToken);
    request.setJsonBody(ReqRes.makeJsonString(newStatusMessage));

    Api api = new Api();
    api.put(request, (error, response) -> {
      if (error != null) {
        this.updateStatus(applicationId, oldStatus);
        Views.displayAlert(AlertType.ERROR, "Connect Error", "Sevrer/Connection error", error.getMessage());
        return null;
      }
      return response;
    });
  }

  // Change all status
  @FXML
  void handleChangeStatusBtn(ActionEvent event) {
    if (selectedStatusMenuItem == null) {
      Views.displayAlert(AlertType.WARNING, "Empty status field", "", "Please set a new status value.");
      return;
    }

    String newStatus = selectedStatusMenuItem.getText().equals("PENDING") ? "PENDING"
        : selectedStatusMenuItem.getText() + "D";
    String authToken;
    try {
      authToken = ReqRes.getAuthenticationString();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return;
    }

    Request request = new Request();
    request.setHeaderMap("Authorization", authToken);
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("api/admin/status/all/?newstatus=" + newStatus);

    Api api = new Api();
    api.get(request, (error, response) -> {
      if (error != null) {
        Views.displayAlert(AlertType.ERROR, "Error", "Connection/Server Error", error.getMessage());
        return null;
      }

      // Accepted
      Message respMessage = (Message) ReqRes.makeModelFromJson(response.body(), Message.class);
      if (response.statusCode() == 202) {
        this.updateStatus(Status.fromString(newStatus));
        Views.displayAlert(AlertType.INFORMATION, "Updated Successful", "", respMessage.getMessage());
      } else {
        Views.displayAlert(AlertType.INFORMATION, "Update unsuccessfull", "", respMessage.getMessage());
      }
      return response;
    });
  }

  @FXML
  void handleNotifyStudentsBtn(ActionEvent event) {
    try {
      AnchorPane sendMessagePane = (AnchorPane) Views.loadFXML("/views/admin_pages/message_page");
      Views.paintPage(sendMessagePane, lowerAnchorPane, 0, 0, 0, 0);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  @FXML
  void handleLogoutBtn(ActionEvent event) {
    Alert alert = Views.displayAlert(AlertType.CONFIRMATION, "Logout", "Confirm to logout", null);
    if (alert.getResult().getText().equals("Cancel"))
      return;
    try {
      // Delete the auth.bat file
      File file = new File("src/main/resources/auth.bat");
      if (file.exists())
        file.delete();

      AnchorPane parent = (AnchorPane) ((Button) event.getSource()).getParent().getParent();
      AnchorPane home = (AnchorPane) Views.loadFXML("/views/login_page");
      Views.paintPage(home, parent, 0, 0, 0, 0);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }


}
