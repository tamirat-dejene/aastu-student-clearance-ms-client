module com.g3 {
  requires javafx.fxml;
  requires java.sql;
  requires org.apache.commons.codec;
  
  requires transitive com.google.gson;
  requires transitive java.net.http;
  requires transitive javafx.graphics;
  requires transitive javafx.controls;
  requires javafx.base;

  opens g3.scms.api to com.google.gson;
  opens g3.scms.model to com.google.gson;
  opens g3.scms.controllers to javafx.fxml;
  opens g3.scms.utils to com.google.gson, javafx.fxml;

  exports g3.scms.controllers;
  exports g3.scms.model;
  exports g3.scms.utils;
  exports g3.scms.api;
}
