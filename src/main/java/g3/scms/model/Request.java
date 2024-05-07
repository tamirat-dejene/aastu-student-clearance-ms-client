package g3.scms.model;

import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;

public class Request {
  private String baseUrl;
  private String path;
  private Map<String, String> headerMap;
  private String jsonBody;

  public Request() {
    baseUrl = null;
    path = null;
    headerMap = null;
    jsonBody = null;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, String> getHeaderMap() {
    return headerMap;
  }

  public void setHeaderMap(Map<String, String> headerMap) {
    this.headerMap = headerMap;
  }

  public void setHeaderMap(String name, String value) {
    var map = new HashMap<String, String>();
    map.put(name, value);
    if(this.headerMap == null) this.headerMap = map;
    else this.headerMap.put(name, value);
  }

  public String getJsonBody() {
    return jsonBody;
  }

  public void setJsonBody(String json) {
    this.jsonBody = json;
  }

  public Builder makeBuilder() {
    var builder = HttpRequest.newBuilder();
    String baseUrl = this.getBaseUrl();
    String path = "";
    if (baseUrl != null) {
      if (this.getPath() != null)
        path = this.getPath();
      try {
        builder.uri(new URI(baseUrl + path));
      } catch (URISyntaxException e) {
        throw new Error(e.getMessage());
      }
    }

    if (this.getHeaderMap() == null)
      return builder;

    var headerMap = this.getHeaderMap();
    var it = headerMap.entrySet().iterator();
    while (it.hasNext()) {
      var current = it.next();
      builder.setHeader(current.getKey(), current.getValue());
    }

    return builder;
  }
}
