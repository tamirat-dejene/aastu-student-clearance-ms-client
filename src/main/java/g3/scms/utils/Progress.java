package g3.scms.utils;

import javafx.scene.control.ProgressIndicator;


public class Progress {
  private static ProgressIndicator progressIndicator = new ProgressIndicator();
  
  public static void showProgress() {
    progressIndicator.setVisible(true);
  }

  public static void hideProgress() {
    progressIndicator.setVisible(false);
  }

}