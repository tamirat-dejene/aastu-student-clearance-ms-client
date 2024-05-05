package g3.scms.model;

public enum College {
  ENGINEERING,
  NATURAL_AND_APPLIED_SCIECNE;

  public static College toEnum(String collegeString) {
    switch (collegeString) {
      case "Engineering": return ENGINEERING;
      case "Natural and Applied Science": return NATURAL_AND_APPLIED_SCIECNE;
    }
    throw new IllegalArgumentException("No enum constant " + College.class + " for value: " + collegeString);
  }
}
