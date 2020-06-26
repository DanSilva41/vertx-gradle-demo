package br.com.silva.vertx.demo;

import br.com.silva.vertx.demo.support.Constants;
import br.com.silva.vertx.demo.verticles.HelloVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/api/v1/hello").handler(this::helloVertx);
    router.get("/api/v1/hello/:name").handler(this::helloName);

    this.deployVerticles();

    vertx.createHttpServer().requestHandler(router).listen(8280);
  }


  private void deployVerticles() {
    vertx.deployVerticle("br.com.silva.vertx.demo.verticles.HelloVerticle", this.createDeploymentOptions());
  }

  private DeploymentOptions createDeploymentOptions() {
    return new DeploymentOptions()
      .setWorker(true)
      .setInstances(4);
  }

  private void helloVertx(RoutingContext routingContext) {
    vertx.eventBus().request(Constants.HELLO_VERTX_ADDRESS, "", reply ->
      routingContext.request().response().end((String) reply.result().body()));
  }

  private void helloName(RoutingContext routingContext) {
    String name = routingContext.pathParam("name");
    vertx.eventBus().request(Constants.HELLO_NAME_VERTX_ADDRESS, name, reply ->
      routingContext.request().response().end((String) reply.result().body()));
  }
}
