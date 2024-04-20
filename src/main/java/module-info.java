@SuppressWarnings("java.errors.incompleteClasspath.severity")
module com.g3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;

    opens com.g3 to javafx.fxml;
    exports com.g3;
}