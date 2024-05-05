package g3.scms.model;

public enum Department {
  ARCHITECTURE,
  CIVIL_ENGINEERING,
  MINING_ENGINEERING,
  CHEMICAL_ENGINEERING,
  ENVIRNONMENTAL_ENGINEERING,
  ELECTROMECHANICAL_ENGINEERING,
  ELECTRICAL_AND_COMPUTER_ENGINEERING,
  MECHANICAL_ENGINEERING,
  SOFTWARE_ENGINEERING,

  BIOTECHNOLOGY_WITH_HONORS,
  GEOLOGY_WITH_HONORS,
  INDUSTRIAL_CHEMISTRY_WITH_HONORS,
  FOOD_SCIENCE_AND_APPLIED_NUTRITION;

  public static Department toEnum(String departmentString) {
    switch (departmentString) {
      case "Architecture": return ARCHITECTURE;
      case "Civil Engineering": return CIVIL_ENGINEERING;
      case "Mining Engineering": return MINING_ENGINEERING;
      case "Chemical Engineering": return CHEMICAL_ENGINEERING;
      case "Environmental Engineering": return ENVIRNONMENTAL_ENGINEERING;
      case "Electromechanical Engineering": return ELECTROMECHANICAL_ENGINEERING;
      case "Electrical and Computer Engineering": return ELECTRICAL_AND_COMPUTER_ENGINEERING;
      case "Mechanical Engineering": return MECHANICAL_ENGINEERING;
      case "Software Engineering": return SOFTWARE_ENGINEERING;
      case "Biotechnology": return BIOTECHNOLOGY_WITH_HONORS;
      case "Geology": return GEOLOGY_WITH_HONORS;
      case "Industrial Chemistry": return INDUSTRIAL_CHEMISTRY_WITH_HONORS;
      case "Food Science and Applied Nutrition": return FOOD_SCIENCE_AND_APPLIED_NUTRITION;       
    }
    throw new IllegalArgumentException("No enum constant " + Department.class + " for value: " + departmentString);
  }
};
