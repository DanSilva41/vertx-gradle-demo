package br.com.silva.vertx.demo;

import br.com.silva.vertx.demo.handler.HelloHandler;
import br.com.silva.vertx.demo.verticle.HelloVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  private static final int DEFAULT_HTTP_PORT = 8290;
  private final String verticleId = UUID.randomUUID().toString();

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/api/v1/hello").handler(HelloHandler::helloVertx);
    router.get("/api/v1/hello/:name").handler(HelloHandler::helloName);

    this.deployVerticles();

    this.configureServer(router);
  }

  private void deployVerticles() {
    vertx.deployVerticle(new HelloVerticle(verticleId));
  }

  private void configureServer(Router router) {
    int httpPort;
    try {
      httpPort = Integer.parseInt(System.getProperty("http.port", String.valueOf(DEFAULT_HTTP_PORT)));
    } catch (NumberFormatException npe) {
      httpPort = DEFAULT_HTTP_PORT;
    }

    vertx.createHttpServer().requestHandler(router).listen(httpPort);
  }
}
