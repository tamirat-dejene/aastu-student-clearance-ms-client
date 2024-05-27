package g3.scms.api;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import g3.scms.model.Request;
import g3.scms.model.Transcript;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;

public class Api {
  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
  private HttpClient client;

  public Api() {
    this(HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build());
  }

  public Api(HttpClient client) {
    this.client = client;
  }

  /**
   * Sends a POST request using the given request and callback.
   *
   * @param request the request object containing the necessary data
   * @param cb      the callback to handle the response
   * @return the result of the callback processing
   */
  public HttpResponse<String> post(Request request, Callback cb) {
    // HttpClient client = HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build();
    HttpResponse<String> response = null;
    Error error = null;

    try {
      Builder builder = request.makeBuilder();
      builder.POST(BodyPublishers.ofString(request.getJsonBody()));
      HttpRequest httpRequest = builder.build();

      response = client.send(httpRequest, BodyHandlers.ofString());
    } catch (HttpTimeoutException e) {
      logError(e);
      error = new Error("Request timed out. Try again later");
    } catch (IOException | InterruptedException | IllegalStateException e) {
      logError(e);
      error = new Error("The server can't be reached! Stay strong, bud!");
    }

    return cb.next(error, response);
  }

  /**
   * Sends a GET request using the given request and callback.
   *
   * @param request the request object containing the necessary data
   * @param cb      the callback to handle the response
   * @return the result of the callback processing
   */
  public HttpResponse<String> get(Request request, Callback cb) {
    // HttpClient client = HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build();
    HttpResponse<String> response = null;
    Error error = null;

    try {
      Builder builder = request.makeBuilder();
      builder.GET();
      HttpRequest httpRequest = builder.build();

      response = client.send(httpRequest, BodyHandlers.ofString());

    } catch (HttpTimeoutException e) {
      logError(e);
      error = new Error("Request timed out. Try again later");
    } catch (IOException | InterruptedException | IllegalStateException e) {
      logError(e);
      error = new Error("Failed to send GET request: " + e.getMessage());
    }

    return cb.next(error, response);
  }

  /**
   * Sends a PUT request using the given request and callback.
   *
   * @param request the request object containing the necessary data
   * @param cb      the callback to handle the response
   * @return the result of the callback processing
   */
  public HttpResponse<String> put(Request request, Callback cb) {
    // HttpClient client = HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build();
    HttpResponse<String> response = null;
    Error error = null;

    try {
      Builder builder = request.makeBuilder();
      builder.PUT(BodyPublishers.ofString(request.getJsonBody()));
      HttpRequest httpRequest = builder.build();

      response = client.send(httpRequest, BodyHandlers.ofString());

    } catch (HttpTimeoutException e) {
      logError(e);
      error = new Error("Request timed out. Try again later");
    } catch (IOException | InterruptedException | IllegalStateException e) {
      logError(e);
      error = new Error("The server can't be reached! Stay strong, bud!");
    }

    return cb.next(error, response);
  }

  /**
   * Sends a DELETE request using the given request and callback.
   *
   * @param request the request object containing the necessary data
   * @param cb      the callback to handle the response
   * @return the result of the callback processing
   */
  public HttpResponse<String> delete(Request request, Callback cb) {
    // HttpClient client = HttpClient.newBuilder().connectTimeout(REQUEST_TIMEOUT).build();
    HttpResponse<String> response = null;
    Error error = null;

    try {
      Builder builder = request.makeBuilder();
      builder.DELETE();
      HttpRequest httpRequest = builder.build();

      response = client.send(httpRequest, BodyHandlers.ofString());

    } catch (HttpTimeoutException e) {
      logError(e);
      error = new Error("Request timed out. Try again later");
    } catch (IOException | InterruptedException | IllegalStateException e) {
      logError(e);
      error = new Error("Failed to send DELETE request: " + e.getMessage());
    }

    return cb.next(error, response);
  }

  /**
   * Logs the error with its class and message.
   * 
   * @param e the exception to be logged
   */
  private void logError(Exception e) {
    System.err.println("Exception occurred: " + e.getClass().getName());
    System.err.println("Message: " + e.getMessage());
    // e.printStackTrace();
  }

  public static void testPostGet() {
    var api = new Api();

    Transcript model = new Transcript();
    model.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");

    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("AssemblyAI_API_BASE_URL"));
    request.setPath("v2/transcript");
    request.setHeaderMap("Authorization", Util.getEnv().getProperty("AssemblyAI_API_AUTH_KEY"));
    request.setJsonBody(ReqRes.makeJsonString(model));

    var response = api.post(request, (err, res) -> {
      if (err != null)
        throw err;
      return res;
    });

    int statusCode = response.statusCode();
    if (statusCode == 200) {
      var m = (Transcript) ReqRes.makeModelFromJson(response.body(), Transcript.class);
      String reqId = m.getId();
      request.setBaseUrl(Util.getEnv().getProperty("AssemblyAI_API_BASE_URL"));
      request.setPath(request.getPath() + "/" + reqId);
      request.setHeaderMap("Authorization", Util.getEnv().getProperty("AssemblyAI_API_AUTH_KEY"));

      Transcript mod;
      while (true) {
        response = api.get(request, (er, res) -> res);
        mod = (Transcript) ReqRes.makeModelFromJson(response.body(), Transcript.class);
        System.out.println(mod.getStatus());
        if ("completed".equals(mod.getStatus()) || "error".equals(mod.getStatus()))
          break;
      }
      if (mod != null)
        System.out.println(mod.getText());
    }
  }

  public static void main(String[] args) throws Exception {
    testPostGet();
    System.out.println(Util.getEnv().getProperty("AssemblyAI_API_AUTH_KEY"));
    System.out.println(Util.getEnv().getProperty("AssemblyAI_API_BASE_URL"));
  }
}
