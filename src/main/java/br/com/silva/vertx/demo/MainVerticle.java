package br.com.silva.vertx.demo;

import br.com.silva.vertx.demo.handler.HelloHandler;
import br.com.silva.vertx.demo.verticle.HelloVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  private final String verticleId = UUID.randomUUID().toString();

  @Override
  public void start(Promise<Void> start) {
    Router router = Router.router(vertx);
    SessionStore store = LocalSessionStore.create(vertx);
    router.route().handler(LoggerHandler.create());
    router.route().handler(SessionHandler.create(store));
    router.route().handler(CorsHandler.create("localhost"));
    router.route().handler(StaticHandler.create("web").setIndexPage("index.html"));
    router.route().handler(CSRFHandler.create("anySecret"));

    router.route().handler(this::verifyAuthToken);
    router.get("/api/v1/hello").handler(HelloHandler::helloVertx);
    router.get("/api/v1/hello/:name").handler(HelloHandler::helloName);

    this.deployVerticles();
    this.configureServer(router, start);
  }

  private void verifyAuthToken(RoutingContext routingContext) {
    String authToken = routingContext.request().getHeader("AUTH_TOKEN");
    if ("mysecretAuthToken".contentEquals(authToken)) {
      routingContext.next();
    } else {
      routingContext.response().setStatusCode(401).setStatusMessage("UNAUTHORIZED").end();
    }
  }

  private void deployVerticles() {
    vertx.deployVerticle(new HelloVerticle(verticleId));
  }

  private void configureServer(Router router, Promise<Void> start) {

    ConfigStoreOptions defaultOptions = new ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(new JsonObject().put("path", "config.json"));

    ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions()
      .addStore(defaultOptions);

    ConfigRetriever configRetriever = ConfigRetriever.create(vertx, retrieverOptions);

    Handler<AsyncResult<JsonObject>> handler = asyncResult -> this.handleConfigResults(asyncResult, start, router);
    configRetriever.getConfig(handler);
  }

  private void handleConfigResults(AsyncResult<JsonObject> asyncResult, Promise<Void> start, Router router) {
    if (asyncResult.succeeded()) {
      JsonObject config = asyncResult.result();
      JsonObject http = config.getJsonObject("http");
      int port = http.getInteger("port");
      vertx.createHttpServer().requestHandler(router).listen(port);
      start.complete();
    } else {
      start.fail("Unable to load configuration.");
    }
  }
}
