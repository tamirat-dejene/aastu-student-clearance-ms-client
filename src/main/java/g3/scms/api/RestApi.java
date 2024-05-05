package g3.scms.api;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;

public class RestApi {

  /**
   * 
   * @param requestComponent
   * @param cb
   * @return HttpResponse<String> 
   */
  public HttpResponse<String> post(Request request, Callback cb) {
    Builder builder;
    try { builder = request.makeBuilder(); } catch (Exception e) { throw e; }

    builder.POST(BodyPublishers.ofString(request.getJsonBody()));  

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = null;
    Error error = null;
    try {
      response = client.send(builder.build(), BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      error = new Error("The server is down! stay strong, bud!");
    }
    return cb.next(error, response);
  }

  public HttpResponse<String> get(Request request, Callback cb) {
    Builder builder;
    try {
      builder = request.makeBuilder();
    } catch (Error e) {
      throw e;
    }

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    try {
      response = client.send(builder.build(), BodyHandlers.ofString());
      return cb.next(null, response);
    } catch (IOException | InterruptedException e) {
      return cb.next(new Error(e.getMessage()), null);
    }
  }

  public HttpResponse<String> put() {
    
    Builder builder = HttpRequest.newBuilder();
    builder.PUT((BodyPublisher) BodyHandlers.ofString());


    return null;
  }

  public static void testPostGet() {
    var api = new RestApi();
    var headers = new HashMap<String, String>();

    headers.put("Authorization", Util.getEnv().getProperty("API_AUTH_KEY"));

    Transcript model = new Transcript();
    model.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");

    Request request = new Request();
    request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
    request.setPath("v2/transcript");
    request.setHeaderMap(headers);
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
      request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
      request.setPath(request.getPath() + "/" + reqId);
      headers.put("Authorization", Util.getEnv().getProperty("API_AUTH_KEY"));
      request.setHeaderMap(headers);

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
    // testPostGet();
  }
}
