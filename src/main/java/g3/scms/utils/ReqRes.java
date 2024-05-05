package g3.scms.utils;

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
}
