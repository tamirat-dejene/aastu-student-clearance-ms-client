package g3.scms.utils;

import org.apache.commons.codec.binary.Hex;

import g3.scms.model.Notification;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class Util {

  public static String generateSalt() {
    return generateSalt(16);
  }

  public static String generateSalt(int byteSize) {
    SecureRandom secureRandom;
    try {
      secureRandom = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      return "";
    }
    byte[] salt = new byte[byteSize];
    secureRandom.nextBytes(salt);
    return Hex.encodeHexString(salt);
  }

  public static Properties getEnv() {
    Properties properties = new Properties();
    try (InputStream input = ClassLoader.getSystemResourceAsStream("config.properties")) {
      if (input != null) {
        properties.load(input);
        return properties;
      } else {
        System.err.println("Unable to find config.properties file.");
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String getDateString(Date date, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
  }

  public static String getDateString() {
    return getDateString(new Date(), "h:mm a, E, MMM dd, yyyy");
  }

  public static String getDateString(Date date) {
    return getDateString(date, "h:mm a, E, MMM dd, yyyy");
  }

  public static Date parseDateString(String date) {
    SimpleDateFormat format = new SimpleDateFormat("h:mm a, E, MMM dd, yyyy");
    try {
      return format.parse(date);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }


  @SuppressWarnings("deprecation")
  public static void main(String[] args) {
    Arrays.asList(
      new Notification("Abbb", "Cddd", getDateString(new Date())),
      new Notification("Bccc", "Cddd", getDateString(new Date())),
      new Notification("Cddd", "Cddd", getDateString(new Date())),
      new Notification("Deee", "Cddd", getDateString(new Date(2022, 3, 12, 2, 34, 4)))
    ).stream().sorted((n1, n2) -> {
      Date d1 = parseDateString(n1.getDate());
      Date d2 = parseDateString(n1.getDate());
      return d1.compareTo(d2);
    }).forEach(n -> System.out.println(n.getTitle()));


  }
}
