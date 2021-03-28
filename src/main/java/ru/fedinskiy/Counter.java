package ru.fedinskiy;

import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

import static ru.fedinskiy.Counter.MAX_SIZE;

public class Counter {
	public static final int MAX_SIZE = 100_000;

	public static void main(String[] args) {
		System.out.println("Starting the application");
		final Vertx vertx = Vertx.vertx();
		HttpServerOptions options = new HttpServerOptions()
				.setMaxFormAttributeSize(MAX_SIZE);
		final HttpServer server = vertx.createHttpServer(options);
		final Router router = Router.router(vertx);
		final Routes routes = new Routes(vertx);
		router.route(HttpMethod.POST, "/counter").handler(BodyHandler.create()).handler(routes::count);
		router.route("/counter").handler(routes::templating);
		router.route("/*").handler(Routes::routeToDefault);
		router.errorHandler(400, Routes::badRequest);

		server.requestHandler(router).listen(8080);
	}
}

class Routes {
	private final Vertx vertx;

	Routes(Vertx vertx) {
		this.vertx = vertx;
	}

	public void templating(RoutingContext context) {
		final TemplateEngine engine = FreeMarkerTemplateEngine.create(vertx, "html");
		Statistics stats = context.get("stats");
		if (stats == null) {
			stats = Statistics.empty();
		}
		final JsonObject json = stats.putToJson(new JsonObject());

		final HttpServerResponse response = context.response();
		response.putHeader("Content-Type", "text/html; charset=UTF-8");
		engine.render(json, "pages/test.html")
				.flatMap(response::send)
				.onFailure(context::fail);
	}

	public static void routeToDefault(RoutingContext context) {
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

	public static void badRequest(RoutingContext context) {
		final HttpServerResponse response = context.response();
		if (response.headWritten()) {
			return;
		}
		final long size = Integer.parseInt(context.request().getHeader("Content-Length"));
		final String errText;

		if (size > MAX_SIZE) { //too big request
			final double coeff = (double)size / (double) MAX_SIZE;
			errText = "Текст больше максимального в " + coeff + " раз. Вернитесь назад и исправьте его.";
		} else {
			errText = "Bad Request";
		}
		response
				.setStatusCode(400)
				.putHeader("Content-Type", "text/html; charset=UTF-8")
				.end(errText);
	}
}
