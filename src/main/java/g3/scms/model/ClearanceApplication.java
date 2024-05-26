package g3.scms.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClearanceApplication {
  private SimpleIntegerProperty applicationId;
  private SimpleStringProperty applicantId;
  private SimpleStringProperty reason;
  private SimpleObjectProperty<Status> status;
  private SimpleStringProperty date;

  public ClearanceApplication(int applicationId, String applicantId, String reason, Status status, String date) {
    this.applicationId = new SimpleIntegerProperty(applicationId);
    this.applicantId = new SimpleStringProperty(applicantId);
    this.reason = new SimpleStringProperty(reason);
    this.status = new SimpleObjectProperty<>(status);
    this.date = new SimpleStringProperty(date);
  }

  public int getApplicationId() {
    return applicationId.get();
  }

  public SimpleIntegerProperty applicationIdProperty() {
    return applicationId;
  }

  public String getApplicantId() {
    return applicantId.get();
  }

  public SimpleStringProperty applicantIdProperty() {
    return applicantId;
  }

  public String getReason() {
    return reason.get();
  }

  public SimpleStringProperty reasonProperty() {
    return reason;
  }

  public Status getStatus() {
    return status.get();
  }

  public SimpleObjectProperty<Status> statusProperty() {
    return status;
  }

  public String getDate() {
    return date.get();
  }

  public SimpleStringProperty dateProperty() {
    return date;
  }

  public void setApplicationId(int applicationId) {
    this.applicationId.set(applicationId);
  }

  public void setApplicantId(String applicantId) {
    this.applicantId.set(applicantId);
  }

  public void setReason(String reason) {
    this.reason.set(reason);
  }

  public void setStatus(Status status) {
    this.status.set(status);
  }

  public void setDate(String date) {
    this.date.set(date);
  }
}
