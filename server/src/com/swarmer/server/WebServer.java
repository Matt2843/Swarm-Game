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
				Map model = new HashMap<>();
				model.put("name", "Sam");
				return render(model, "/views/pages/index.hbs");
			}
		});

		get("hello", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				Map model = new HashMap<>();
				model.put("name", "Sam");
				return "heeey";
			}
		});

	}

	private static String render(Map model, String templatePath) {
		return new HandlebarsTemplateEngine("/").render(new ModelAndView(model, templatePath));
	}
}