package g3.scms.model;

public class Student {
  private String firstName, middleName, lastName, idNumber, section;
  private int classYear;
  private College college;
  private Department department;

  public Student(String firstName, String middleName, String lastName, String idNumber, String section, int classYear,
      College college, Department department) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.idNumber = idNumber;
    this.section = section;
    this.classYear = classYear;
    this.college = college;
    this.department = department;
  }

  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getMiddleName() {
    return middleName;
  }
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getIdNumber() {
    return idNumber;
  }
  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }
  public String getSection() {
    return section;
  }
  public void setSection(String section) {
    this.section = section;
  }
  public int getClassYear() {
    return classYear;
  }
  public void setClassYear(int classYear) {
    this.classYear = classYear;
  }
  public College getCollege() {
    return college;
  }
  public void setCollege(College college) {
    this.college = college;
  }
  public Department getDepartment() {
    return department;
  }
  public void setDepartment(Department department) {
    this.department = department;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((idNumber == null) ? 0 : idNumber.hashCode());
    result = prime * result + ((section == null) ? 0 : section.hashCode());
    result = prime * result + classYear;
    result = prime * result + ((college == null) ? 0 : college.hashCode());
    result = prime * result + ((department == null) ? 0 : department.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Student other = (Student) obj;
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    } else if (!firstName.equals(other.firstName))
      return false;
    if (middleName == null) {
      if (other.middleName != null)
        return false;
    } else if (!middleName.equals(other.middleName))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (idNumber == null) {
      if (other.idNumber != null)
        return false;
    } else if (!idNumber.equals(other.idNumber))
      return false;
    if (section == null) {
      if (other.section != null)
        return false;
    } else if (!section.equals(other.section))
      return false;
    if (classYear != other.classYear)
      return false;
    if (college != other.college)
      return false;
    if (department != other.department)
      return false;
    return true;
  }

}