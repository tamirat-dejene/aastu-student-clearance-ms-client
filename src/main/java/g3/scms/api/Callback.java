package g3.scms.api;

import java.net.http.HttpResponse;

@FunctionalInterface
public interface Callback {
  /**
   * 
   * @param error
   * @param response
   * @return http response
   */
  public HttpResponse<String> next(Error error, HttpResponse<String> response);
}
