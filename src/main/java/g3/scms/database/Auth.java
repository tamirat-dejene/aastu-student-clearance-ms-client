package g3.scms.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import g3.scms.model.College;
import g3.scms.model.Department;
import g3.scms.model.Student;

public class Auth {

  static void test1() {  }

  static void test2() {
    Student student1 = new Student(
        "Tamirat", "Dejenie", "Wondimu",
        "ETS1518/14", "E", 3,
        College.ENGINEERING, Department.SOFTWARE_ENGINEERING);

    System.out.println(student1.getCollege());
    System.out.println(student1.getDepartment());
  }
  
  static void test3() {
    Properties properties = new Properties();
    try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
      properties.load(input);
      var iterator = properties.entrySet().iterator();
      while (iterator.hasNext()) {
        var curr = iterator.next();
        System.out.println(curr.getKey() + ": "+curr.getValue());
      }
    } catch (IOException e) {
  
    }
  }
  
  public static void main(String[] args) {
  }
}
