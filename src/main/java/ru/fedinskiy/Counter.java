package ru.fedinskiy;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

public class Counter {
	public static void main(String[] args) {
		final Vertx vertx = Vertx.vertx();
		final HttpServer server = vertx.createHttpServer();
		final Router router = Router.router(vertx);
		final Routes routes = new Routes(vertx);
		router.route(HttpMethod.POST, "/counter").handler(BodyHandler.create()).handler(routes::count);
		router.route("/counter").handler(routes::templating);
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
		final TemplateEngine engine = FreeMarkerTemplateEngine.create(vertx,"html");
		Statistics stats = context.get("stats");
		if(stats==null){
			stats=Statistics.empty();
		}
		final JsonObject json = stats.putToJson( new JsonObject());

		final HttpServerResponse response = context.response();
		response.putHeader("Content-Type","text/html; charset=UTF-8");
		engine.render(json, "pages/test.html")
				.flatMap(response::send)
				.onFailure(context::fail);
	}

	public static void routeToDefault(RoutingContext context){
		context.redirect("/counter");
		context.redirect("back");
	}

	public void count(RoutingContext context) {
		final HttpServerRequest request = context.request();
		final String input = request.getFormAttribute("input");
		final Statistics statistics = Statistics.fromString(input);
		context.put("stats", statistics);
		context.next();
	}
}
