package g3.scms.controllers;

import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class SignupController {
  @FXML private RadioMenuItem admissionCep;
  @FXML private RadioMenuItem admissionRegular;
  @FXML private MenuButton admissionType;
  @FXML private ToggleGroup admissionTypeChoice;
  @FXML private Button backToLoginBtn;
  @FXML private TextField classYear;
  @FXML private RadioMenuItem colAs;
  @FXML private RadioMenuItem colEng;
  @FXML private MenuButton college;
  @FXML private ToggleGroup collegeChoice;
  @FXML private RadioMenuItem degGrad;
  @FXML private RadioMenuItem degPost;
  @FXML private RadioMenuItem degUnder;
  @FXML private MenuButton degree;
  @FXML private ToggleGroup degreeChoice;
  @FXML private MenuButton department;
  @FXML private ToggleGroup departmentChoice;
  @FXML private RadioMenuItem deptArch;
  @FXML private RadioMenuItem deptBio;
  @FXML private RadioMenuItem deptCeng;
  @FXML private RadioMenuItem deptChemE;
  @FXML private RadioMenuItem deptEce;
  @FXML private RadioMenuItem deptEme;
  @FXML private RadioMenuItem deptEnv;
  @FXML private RadioMenuItem deptFs;
  @FXML private RadioMenuItem deptGeo;
  @FXML private RadioMenuItem deptIc;
  @FXML private RadioMenuItem deptMeE;
  @FXML private RadioMenuItem deptMining;
  @FXML private RadioMenuItem deptSwe;
  @FXML private TextField emailAddress;
  @FXML private TextField firstName;
  @FXML private TextField idNumber;
  @FXML private TextField lastName;
  @FXML private TextField middleName;
  @FXML private PasswordField password;
  @FXML private Button proceedBtn;
  @FXML private TextField section;
  
  @FXML private Label admissionTypeError;
  @FXML private Label classYearError;
  @FXML private Label collegeError;
  @FXML private Label degreeError;
  @FXML private Label departmentError;
  @FXML private Label emailError;
  @FXML private Label firstNameError;
  @FXML private Label idNumberError;
  @FXML private Label lastNameError;
  @FXML private Label middleNameError;
  @FXML private Label passwordError;
  @FXML private Label sectionError;
  
  @FXML void handleBackToLoginBtn(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/landing_page.fxml"));
        AnchorPane loginPane = loader.load();
        loginPane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        
        AnchorPane.setTopAnchor(loginPane, 0.0);
        AnchorPane.setBottomAnchor(loginPane, 0.0);
        AnchorPane.setLeftAnchor(loginPane, 0.0);
        AnchorPane.setRightAnchor(loginPane, 0.0);
        
        //inputFieldAnchorPane.getChildren().clear();
        //inputFieldAnchorPane.getChildren().add(loginPane);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
  }
  @FXML void handleProceedBtn(ActionEvent event) {

  }

}
