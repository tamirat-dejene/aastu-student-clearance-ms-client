package g3.scms.utils;

import org.apache.commons.codec.binary.Hex;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
    try (InputStream input = new FileInputStream("aastu_scms/src/main/resources/config.properties")) {
      properties.load(input);
      return properties;
    } catch (IOException e) {
      return null;
    }
  }

}
