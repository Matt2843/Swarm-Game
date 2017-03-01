package com.swarmer.server;

import spark.Request;
import spark.Response;
import spark.Route;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.staticFiles;

public class WebServer {
	public static void main(String[] args) {

		staticFiles.location("/public");

		get("template-example", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				Map model = new HashMap<>();
				model.put("name", "Sam");
				return render(model, "/views/pages/app.hbs");
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

		get("servers", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				return "localhost:1234";
			}
		});

	}

	private static String render(Map model, String templatePath) {
		return new HandlebarsTemplateEngine("/").render(new ModelAndView(model, templatePath));
	}
}