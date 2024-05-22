package g3.scms.utils;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
