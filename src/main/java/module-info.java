module com.g3 {
  // requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  // requires commons.crypto;
  // requires mysql.connector.j;
  requires org.apache.commons.codec;
  requires org.kordamp.bootstrapfx.core;
  
  requires java.net.http;
  requires com.google.gson;
  requires transitive javafx.graphics;
  requires transitive javafx.controls;

  opens g3.scms.api to com.google.gson;
  opens g3.scms.model to com.google.gson;
  opens g3.scms.controllers to javafx.fxml;
  opens g3.scms.utils to com.google.gson, javafx.fxml;

  exports g3.scms.controllers;
  exports g3.scms.database;
  exports g3.scms.model;
  exports g3.scms.utils;
  exports g3.scms.api;
}
