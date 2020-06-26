package br.com.silva.vertx.demo.verticle;

import br.com.silva.vertx.demo.support.Constants;
import io.vertx.core.AbstractVerticle;

public class HelloVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.eventBus().consumer(Constants.HELLO_VERTX_ADDRESS, msg ->
      msg.reply("Hello, I'm trying Vert.x with routers and eventBus"));

    vertx.eventBus().consumer(Constants.HELLO_NAME_VERTX_ADDRESS, msg -> {
      String name = (String) msg.body();
      msg.reply(String.format("Hello %s, you're trying Vert.x with routers and eventBus", name));
    });
  }
}
