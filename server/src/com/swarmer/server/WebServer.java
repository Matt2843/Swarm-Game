package com.swarmer.server;

import spark.Request;
import spark.Response;
import spark.Route;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

public class WebServer {
	public static void main(String[] args) {

		get("template-example", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				Map<String, Object> model = new HashMap<>();
				return render(model, "server/views/pages/index.hbs");
			}
		});

	}

	private static String render(Map<String, Object> model, String templatePath) {
		return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
	}
}