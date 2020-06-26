package br.com.silva.vertx.demo.verticle;

import br.com.silva.vertx.demo.support.Constants;
import io.vertx.core.AbstractVerticle;

public class HelloVerticle extends AbstractVerticle {

  private final String verticleId;

  public HelloVerticle(final String verticleId) {
    this.verticleId = verticleId;
  }

  @Override
  public void start() {
    vertx.eventBus().consumer(Constants.HELLO_VERTX_ADDRESS, msg ->
      msg.reply(String.format("Server: %s :: Hello, I'm trying Vert.x with routers and eventBus", this.verticleId)));

    vertx.eventBus().consumer(Constants.HELLO_NAME_VERTX_ADDRESS, msg -> {
      String name = (String) msg.body();
      msg.reply(String.format("Server: %s :: Hello %s, you're trying Vert.x with routers and eventBus", this.verticleId, name));
    });
  }
}
