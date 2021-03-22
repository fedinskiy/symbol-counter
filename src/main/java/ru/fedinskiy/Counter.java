package ru.fedinskiy;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.httl.HTTLTemplateEngine;

public class Counter {
	public static void main(String[] args) {
		final Vertx vertx = Vertx.vertx();
		final HttpServer server = vertx.createHttpServer();
		final Router router = Router.router(vertx);
		final Routes routes = new Routes(vertx);
		router.route("/hi").handler(routes::templating);
		router.route("/*").handler(Routes::routeToDefault);

		server.requestHandler(router).listen(8080);
	}
}

class Routes {
	private final Vertx vertx;

	Routes(Vertx vertx) {
		this.vertx = vertx;
	}

	public void templating(RoutingContext context){
		TemplateEngine engine = HTTLTemplateEngine.create(vertx);
		final JsonObject json = new JsonObject()
				.put("key","world");
		engine.render(json, "pages/test.httl")
		.onFailure(context::fail)
		.onSuccess(buffer -> {
			context.response()
					.send(buffer).onFailure(context::fail);
		});
	}

	public static void hello(RoutingContext context){
		// This handler gets called for each request that arrives on the server
		HttpServerResponse response = context.response();
		response.putHeader("content-type", "text/plain");

		// Write to the response and end it
		response.end("Hello World!");
	}

	public static void routeToDefault(RoutingContext context){
		context.redirect("/hi");
		context.redirect("back");
	}
}
