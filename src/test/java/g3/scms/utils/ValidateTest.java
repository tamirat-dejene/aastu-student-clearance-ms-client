package g3.scms.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidateTest {
  @Test
  public void testValidName() {
    assertTrue(Validate.name("abbi"));
  }

  @Test
  public void testInvalidName() {
    Error exception = assertThrows(Error.class, () -> {
      Validate.name("abbi123");
    });
    assertEquals("Invalid name", exception.getMessage());
  }

  @Test
  public void testValidEmail() {
    assertTrue(Validate.email("abbi.dejene@aastustudent.edu.et"));
  }

  @Test
  public void testInvalidEmail() {
    Error exception = assertThrows(Error.class, () -> {
      Validate.email("abbi.dejene@gmail.com");
    });
    assertEquals("Invalid email address", exception.getMessage());
  }

  @Test
  public void testValidIdNumber() {
    assertTrue(Validate.idNumber("ETS1234/56"));
  }

  @Test
  public void testInvalidIdNumber() {
    Error exception = assertThrows(Error.class, () -> {
      Validate.idNumber("1234/56");
    });
    assertEquals("Invalid id#", exception.getMessage());
  }

  @Test
  public void testValidPassword() {
    assertTrue(Validate.password("strongpassword"));
  }

  @Test
  public void testInvalidPassword() {
    Error exception = assertThrows(Error.class, () -> {
      Validate.password("123");
    });
    assertEquals("length must be > 6", exception.getMessage());
  }

  @Test
  public void testPasswordInvalid() {
    assertThrows(Error.class, () -> Validate.password("12345"));
    assertThrows(Error.class, () -> Validate.password(""));
  }

  @Test
  public void testSectionValid() {
    assertTrue(Validate.section("a1"));
    assertTrue(Validate.section("b2"));
  }

  @Test
  public void testSectionInvalid() {
    assertThrows(Error.class, () -> Validate.section("A-1"));
    assertThrows(Error.class, () -> Validate.section("123"));
    assertThrows(Error.class, () -> Validate.section(""));
  }

  @Test
  public void testClassYearValid() {
    assertTrue(Validate.classYear("1"));
    assertTrue(Validate.classYear("10"));
  }

  @Test
  public void testClassYearInvalid() {
    assertThrows(Error.class, () -> Validate.classYear("0"));
    assertThrows(Error.class, () -> Validate.classYear("11"));
    assertThrows(Error.class, () -> Validate.classYear("abc"));
  }

  @Test
  public void testAppNumberValid() {
    assertTrue(Validate.appNumber("AASTUSCMS-123"));
    assertTrue(Validate.appNumber("AASTUSCMS-456"));
  }

  @Test
  public void testAppNumberInvalid() {
    assertThrows(Error.class, () -> Validate.appNumber("123"));
    assertThrows(Error.class, () -> Validate.appNumber("AASTUSCMS-"));
    assertThrows(Error.class, () -> Validate.appNumber("AASTUSCMS-ABC"));
  }

  @Test
  public void testDepartmentIdValid() {
    assertTrue(Validate.departmentId("REGISTRAR001"));
    assertTrue(Validate.departmentId("DORMITORY001"));
  }

  @Test
  public void testDepartmentIdInvalid() {
    assertThrows(Error.class, () -> Validate.departmentId("INVALID001"));
    assertThrows(Error.class, () -> Validate.departmentId("CAFETERIA002"));
  }

  @Test
  public void testDepartmentPwdValid() {
    assertTrue(Validate.departmentPwd("password"));
    assertTrue(Validate.departmentPwd("secure123"));
  }

  @Test
  public void testDepartmentPwdInvalid() {
    assertThrows(Error.class, () -> Validate.departmentPwd("12345"));
    assertThrows(Error.class, () -> Validate.departmentPwd(""));
  }
}