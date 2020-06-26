package br.com.silva.vertx.demo;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello, I'm trying Vert.x"))
        .listen(8280);
  }

}
