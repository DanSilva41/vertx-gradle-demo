package br.com.silva.vertx.demo;

import br.com.silva.vertx.demo.handler.HelloHandler;
import br.com.silva.vertx.demo.verticle.HelloVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/api/v1/hello").handler(HelloHandler::helloVertx);
    router.get("/api/v1/hello/:name").handler(HelloHandler::helloName);

    this.deployVerticles();

    vertx.createHttpServer().requestHandler(router).listen(8280);
  }


  private void deployVerticles() {
    vertx.deployVerticle(new HelloVerticle());
  }
}
