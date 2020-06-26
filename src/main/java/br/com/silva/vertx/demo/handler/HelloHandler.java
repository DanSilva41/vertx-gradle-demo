package br.com.silva.vertx.demo.handler;

import br.com.silva.vertx.demo.support.Constants;
import io.vertx.ext.web.RoutingContext;

public class HelloHandler {

  public static void helloVertx(RoutingContext routingContext) {
    routingContext.vertx().eventBus().request(Constants.HELLO_VERTX_ADDRESS, "", reply ->
      routingContext.request().response().end((String) reply.result().body()));
  }

  public static void helloName(RoutingContext routingContext) {
    String name = routingContext.pathParam("name");
    routingContext.vertx().eventBus().request(Constants.HELLO_NAME_VERTX_ADDRESS, name, reply ->
      routingContext.request().response().end((String) reply.result().body()));
  }
}
