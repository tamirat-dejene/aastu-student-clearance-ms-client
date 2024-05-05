package g3.scms.model;

public enum Degree {
  UNDERGRADUATE,
  GRADUATE,
  POSTGRADUATE;

  public static Degree toEnum(String degreeString) {
    switch (degreeString) {
      case "Undergraduate": return UNDERGRADUATE;
      case "Graduate": return GRADUATE;
      case "Postgraduate": return POSTGRADUATE;
    }
    throw new IllegalArgumentException("No enum constant " + Degree.class + " for value: " + degreeString);
  }
}
