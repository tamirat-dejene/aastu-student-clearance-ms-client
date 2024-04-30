package g3.scms.database;

import java.io.IOException;
import java.security.GeneralSecurityException;

import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.crypto.random.CryptoRandom;
import org.apache.commons.crypto.random.CryptoRandomFactory;

public class Auth {

  public static void main(String[] args) {
    final byte[] key = new byte[16];
    final byte[] iv = new byte[32];
    final Properties properties = new Properties();

    properties.put(CryptoRandomFactory.CLASSES_KEY, CryptoRandomFactory.RandomProvider.OPENSSL.getClassName());

    try (CryptoRandom random = CryptoRandomFactory.getCryptoRandom(properties)) {
      System.out.println(random.getClass().getCanonicalName());
      random.nextBytes(key);
      random.nextBytes(iv);
    } catch (GeneralSecurityException | IOException exception) {
      System.out.println(exception.getMessage());
    }

    System.out.println(Arrays.toString(key));
    System.out.println(Arrays.toString(iv));

  }

}
