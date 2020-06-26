package br.com.silva.vertx.demo;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
class MainVerticleTest {

  @BeforeEach
  void prepare(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(MainVerticle.class.getCanonicalName(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  @DisplayName("Check that the server return hello Vert.x")
  void checkServerReturnHello(Vertx vertx, VertxTestContext testContext) {
    WebClient webClient = WebClient.create(vertx);
    webClient.get(8280, "localhost", "/api/v1/hello")
      .as(BodyCodec.string())
      .send(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        assertTrue(response.body().length() > 0);
        assertTrue(response.body().contains("Hello, I'm trying Vert.x with routers and eventBus"));
        testContext.completeNow();
      })));
  }

  @Test
  @DisplayName("Check that the server return hello by name")
  void checkServerReturnHelloByName(Vertx vertx, VertxTestContext testContext) {
    WebClient webClient = WebClient.create(vertx);
    webClient.get(8280, "localhost", "/api/v1/hello/Dan")
      .as(BodyCodec.string())
      .send(testContext.succeeding(response -> testContext.verify(() -> {
        assertEquals(200, response.statusCode());
        assertTrue(response.body().length() > 0);
        assertTrue(response.body().contains("Hello Dan, you're trying Vert.x with routers and eventBus"));
        testContext.completeNow();
      })));
  }
}
