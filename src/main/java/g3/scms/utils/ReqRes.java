package g3.scms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.google.gson.Gson;

public class ReqRes {

  public static String makeJsonString(Object obj) {
    Gson gson = new Gson();
    return gson.toJson(obj);
  }

  public static Object makeModelFromJson(String jsonString, java.lang.reflect.Type type) {
    try {
      Gson gson = new Gson();
      return gson.fromJson(jsonString, type);
    } catch (Exception e) {
      System.out.println("Error");
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static String getAuthenticationString() throws IOException {
    FileInputStream input = new FileInputStream("aastu_scms/src/main/resources/auth.bat");
    String token = "";
    var temp = 0;
    while ((temp = input.read()) != -1)
      token += (char) temp;
    input.close();
    return token;
  }
  public static String getAuthenticationString(File sessionFile) throws IOException {
    FileInputStream input = new FileInputStream(sessionFile);
    String token = "";
    var temp = 0;
    while ((temp = input.read()) != -1)
      token += (char) temp;
    input.close();
    return token;
  }
}
