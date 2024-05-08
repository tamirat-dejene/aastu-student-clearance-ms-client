package g3.scms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

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

  public static String getAuthenticationString(File file) throws IOException {
    FileInputStream input = new FileInputStream(file);
    StringBuilder tokenBuilder = new StringBuilder();
    int temp;
    while ((temp = input.read()) != -1)
      tokenBuilder.append((char) temp);

    input.close();
    return tokenBuilder.toString();
  }

  public static String getAuthenticationString() throws IOException {
    File file = new File("aastu_scms/src/main/resources/auth.bat");
    return ReqRes.getAuthenticationString(file);
  }

  public static void main(String[] args) throws URISyntaxException {
    try {
      System.out.println(ReqRes.getAuthenticationString());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
