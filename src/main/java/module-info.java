module com.g3 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires mysql.connector.j;

  opens g3.scms.controllers to javafx.fxml;
  opens g3.scms.database;

  exports g3.scms.controllers;
  exports g3.scms.database;
}
