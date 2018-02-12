package org.rapidpm.vaadin.ui.app;

import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedBiFunction;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.model.Result;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

import static org.apache.commons.io.IOUtils.copy;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;
import static org.rapidpm.vaadin.ui.app.PWAServlet.*;

/**
 *
 */
@WebServlet(asyncSupported = true,
            urlPatterns = {
                SLASH + SERVICE_WORKER,
                SLASH + MANIFEST,
                SLASH + VAADIN + SLASH + APP_JS,
            }
)
public class PWAServlet extends HttpServlet implements HasLogger {


  public static final String SLASH          = "/";
  public static final String SERVICE_WORKER = "sw.js";
  public static final String APP_JS         = "app.js";
  public static final String MANIFEST       = "manifest.json";
  public static final String VAADIN         = "VAADIN";


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    serveDataFile(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    serveDataFile(req, resp);
  }


  private CheckedFunction<String, InputStream> asStream() {
    return PWAServlet.class::getResourceAsStream;
  }

  private CheckedBiFunction<InputStream, HttpServletResponse, Integer> write() {
    return (inputStream, httpServletResponse) -> copy(inputStream, httpServletResponse.getOutputStream());
  }

  private void serveDataFile(HttpServletRequest request, HttpServletResponse response) {
    response.setCharacterEncoding("utf-8");
    final String url = request.getRequestURL().toString();
    logger().info("url for = " + url);
    match(
        matchCase(() -> failure("nothing matched " + url)),
        matchCase(() -> url.contains(SERVICE_WORKER), () -> success(SLASH + SERVICE_WORKER)),
        matchCase(() -> url.contains(APP_JS), () -> success(SLASH + VAADIN + SLASH + APP_JS)),
        matchCase(() -> url.contains(MANIFEST), () -> success(SLASH + MANIFEST))
    ).ifPresentOrElse(
        resourceToLoad -> {
          final Result<InputStream> ressourceStream = asStream().apply(resourceToLoad);
          ressourceStream.ifAbsent(() -> logger().warning("resource was not available try to load default.."));

          ressourceStream.ifPresent(inputStream -> {
            match(
                matchCase(() -> success("text/plain")),
                matchCase(() -> resourceToLoad.endsWith("js"), () -> success("application/javascript")),
                matchCase(() -> resourceToLoad.endsWith("json"), () -> success("application/json"))
            )
                .ifPresent(response::setContentType);
            write().apply(inputStream, response);

          });
        },
        failed -> logger().warning("wrong ressource requested .. failed .. " + failed)
    );
  }
}
