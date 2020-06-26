package br.com.silva.vertx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/api/v1/hello").handler(this::helloVertx);
    router.get("/api/v1/hello/:name").handler(this::helloName);

    vertx.createHttpServer().requestHandler(router).listen(8280);
  }

  private void helloVertx(RoutingContext routingContext) {
    routingContext.request().response().end("Hello, I'm trying Vert.x with routers");
  }

  private void helloName(RoutingContext routingContext) {
    String name = routingContext.pathParam("name");
    routingContext.request().response().end(String.format("Hello %s, you're trying Vert.x with routers", name));
  }
}
