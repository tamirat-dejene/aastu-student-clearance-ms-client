package g3.scms.model;

public enum AdmissionType {
  CEP,
  REGULAR;
  public static AdmissionType toEnum(String admissionString) { 
    switch (admissionString) {
      case "Regular": return REGULAR;
      case "CEP": return CEP;
    }
    throw new IllegalArgumentException("No enum constant " + AdmissionType.class + " for value: " + admissionString);
  }
  
}
